package lv.venta.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import lombok.extern.slf4j.Slf4j;
import lv.venta.model.MyUser;
import lv.venta.repo.IMyUserRepo;

@Slf4j
@Service
public class TwoFactorAuthService {

    @Autowired
    private IMyUserRepo userRepo;

    private final GoogleAuthenticator gAuth;

    // Constructor with custom config for more lenient verification
    public TwoFactorAuthService() {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder()
                .setTimeStepSizeInMillis(30000) // 30 seconds
                .setWindowSize(5) // Allow ±5 time windows (±2.5 minutes)
                .setCodeDigits(6)
                .build();
        this.gAuth = new GoogleAuthenticator(config);
    }

    /**
     * Generate a new secret key for a user
     */
    @Transactional
    public String generateSecretKey(String username) {
        log.info("Generating new 2FA secret key for user: {}", username);
        
        MyUser user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found: {}", username);
            throw new RuntimeException("User not found");
        }

        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secret = key.getKey();
        
        // CRITICAL: Save the secret key immediately and flush to database
        user.setSecretKey(secret);
        user.setTwoFactorEnabled(false); // Don't enable yet
        userRepo.save(user);
        
        // Verify it was saved
        MyUser verifyUser = userRepo.findByUsername(username);
        log.info("Secret key saved for user: {} (verified secret exists: {})", 
            username, verifyUser.getSecretKey() != null);
        
        return secret;
    }

    /**
     * Generate QR code URL for Google Authenticator
     */
    public String getQRCodeUrl(String username, String secret) {
        try {
            String issuer = "CertMaster";
            String encodedIssuer = URLEncoder.encode(issuer, "UTF-8");
            String encodedUsername = URLEncoder.encode(username, "UTF-8");
            
            String url = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s",
                encodedIssuer,
                encodedUsername,
                secret,
                encodedIssuer
            );
            
            log.debug("Generated OTP URL: {}", url);
            return url;
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding QR URL", e);
            throw new RuntimeException("Error generating QR code URL", e);
        }
    }

    /**
     * Generate QR code image as Base64 string
     */
    public String generateQRCodeImage(String username, String secret) throws WriterException, IOException {
        log.info("Generating QR code image for user: {}", username);
        
        String otpAuthUrl = getQRCodeUrl(username, secret);
        log.info("OTP Auth URL: {}", otpAuthUrl);
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        
        BitMatrix bitMatrix = qrCodeWriter.encode(
            otpAuthUrl, 
            BarcodeFormat.QR_CODE, 
            400,
            400,
            hints
        );
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        log.info("QR code image generated successfully (size: {} bytes)", base64Image.length());
        
        return "data:image/png;base64," + base64Image;
    }

    /**
     * Verify the TOTP code entered by user - FIXED VERSION
     */
    public boolean verifyCode(String username, int code) {
        log.info("Verifying 2FA code for user: {}", username);
        
        MyUser user = userRepo.findByUsername(username);
        if (user == null) {
            log.warn("User not found: {}", username);
            return false;
        }
        
        String secretKey = user.getSecretKey();
        if (secretKey == null || secretKey.isEmpty()) {
            log.error("Secret key not set for user: {}", username);
            return false;
        }

        log.info("Verifying code {} against secret for user {}", code, username);
        log.debug("Secret key length: {}", secretKey.length());
        
        // Use the configured GoogleAuthenticator with wider time window
        boolean isValid = gAuth.authorize(secretKey, code);
        
        if (isValid) {
            log.info("✓ 2FA code verified successfully for user: {}", username);
        } else {
            log.warn("✗ Invalid 2FA code for user: {} (code: {})", username, code);
            
            // Additional debugging
            long currentTime = System.currentTimeMillis() / 30000L;
            log.debug("Current time window: {}", currentTime);
            
            // Try to help debug by checking adjacent windows
            for (int offset = -2; offset <= 2; offset++) {
                int expectedCode = gAuth.getTotpPassword(secretKey, currentTime + offset);
                log.debug("Time offset {}: expected code would be {}", offset, expectedCode);
            }
        }
        
        return isValid;
    }

    /**
     * Check if user has 2FA enabled
     */
    public boolean is2FAEnabled(String username) {
        MyUser user = userRepo.findByUsername(username);
        if (user == null) {
            log.warn("User not found when checking 2FA status: {}", username);
            return false;
        }
        
        boolean enabled = user.isTwoFactorEnabled();
        boolean hasSecret = user.getSecretKey() != null && !user.getSecretKey().isEmpty();
        
        log.info("2FA status for user {}: enabled={}, hasSecret={}", username, enabled, hasSecret);
        
        // User must have BOTH flag enabled AND a secret key
        return enabled && hasSecret;
    }

    /**
     * Disable 2FA for a user
     */
    @Transactional
    public void disable2FA(String username) {
        log.info("Disabling 2FA for user: {}", username);
        
        MyUser user = userRepo.findByUsername(username);
        if (user != null) {
            user.setTwoFactorEnabled(false);
            user.setSecretKey(null);
            userRepo.save(user);
            log.info("2FA disabled for user: {}", username);
        }
    }
    
    /**
     * Get current TOTP code for debugging (REMOVE IN PRODUCTION)
     */
    public int getCurrentCode(String username) {
        MyUser user = userRepo.findByUsername(username);
        if (user == null || user.getSecretKey() == null) {
            return -1;
        }
        return gAuth.getTotpPassword(user.getSecretKey());
    }
}