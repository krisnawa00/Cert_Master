package lv.venta.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import lombok.extern.slf4j.Slf4j;
import lv.venta.model.MyUser;
import lv.venta.repo.IMyUserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Service
public class TwoFactorAuthService {

    @Autowired
    private IMyUserRepo userRepo;

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    /**
     * Generate a new secret key for a user
     */
    public String generateSecretKey(String username) {
        log.info("Generating new 2FA secret key for user: {}", username);
        
        MyUser user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found: {}", username);
            throw new RuntimeException("User not found");
        }

        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secret = key.getKey();
        
        user.setSecretKey(secret);
        user.setTwoFactorEnabled(false); // Will be enabled after first successful verification
        userRepo.save(user);
        
        log.info("Secret key generated and saved for user: {}", username);
        return secret;
    }

    /**
     * Generate QR code URL for Google Authenticator
     */
    public String getQRCodeUrl(String username, String secret) {
        log.debug("Generating QR code URL for user: {}", username);
        return GoogleAuthenticatorQRGenerator.getOtpAuthURL(
            "CertMaster", // Issuer name (appears in authenticator app)
            username,     // Account name
            new GoogleAuthenticatorKey.Builder(secret).build()
        );
    }

    /**
     * Generate QR code image as Base64 string
     */
    public String generateQRCodeImage(String username, String secret) throws WriterException, IOException {
        log.info("Generating QR code image for user: {}", username);
        
        String otpAuthUrl = getQRCodeUrl(username, secret);
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthUrl, BarcodeFormat.QR_CODE, 300, 300);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        
        String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        log.debug("QR code image generated successfully");
        
        return "data:image/png;base64," + base64Image;
    }

    /**
     * Verify the TOTP code entered by user
     */
    public boolean verifyCode(String username, int code) {
        log.info("Verifying 2FA code for user: {}", username);
        
        MyUser user = userRepo.findByUsername(username);
        if (user == null || user.getSecretKey() == null) {
            log.warn("User not found or secret key not set: {}", username);
            return false;
        }

        boolean isValid = gAuth.authorize(user.getSecretKey(), code);
        
        if (isValid) {
            log.info("2FA code verified successfully for user: {}", username);
            
            // Enable 2FA if first time verification
            if (!user.isTwoFactorEnabled()) {
                user.setTwoFactorEnabled(true);
                userRepo.save(user);
                log.info("2FA enabled for user: {}", username);
            }
        } else {
            log.warn("Invalid 2FA code for user: {}", username);
        }
        
        return isValid;
    }

    /**
     * Check if user has 2FA enabled
     */
    public boolean is2FAEnabled(String username) {
        MyUser user = userRepo.findByUsername(username);
        return user != null && user.isTwoFactorEnabled();
    }

    /**
     * Disable 2FA for a user
     */
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
}