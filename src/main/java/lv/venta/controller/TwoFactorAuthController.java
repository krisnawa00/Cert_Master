package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import lv.venta.model.MyUser;
import lv.venta.repo.IMyUserRepo;
import lv.venta.service.impl.TwoFactorAuthService;

@Slf4j
@Controller
@RequestMapping("/2fa")
public class TwoFactorAuthController {

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private IMyUserRepo userRepo;

    /**
     * Setup page - Generate QR code for first time setup
     */
    @GetMapping("/setup")
    public String showSetupPage(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        log.info("=== User {} accessing 2FA setup page ===", username);

        try {
            MyUser user = userRepo.findByUsername(username);
            if (user == null) {
                log.error("User not found: {}", username);
                model.addAttribute("error", "User not found");
                return "error-page";
            }
            
            String secret;
            
            if (user.getSecretKey() == null || user.getSecretKey().isEmpty()) {
                log.info("No secret key exists, generating new one for user: {}", username);
                secret = twoFactorAuthService.generateSecretKey(username);
            } else {
                log.info("Using existing secret key for user: {}", username);
                secret = user.getSecretKey();
            }
            
            // Generate QR code image
            String qrCodeImage = twoFactorAuthService.generateQRCodeImage(username, secret);
            
            model.addAttribute("username", username);
            model.addAttribute("qrCode", qrCodeImage);
            model.addAttribute("secretKey", secret);
            
            // DEBUG: Show current expected code (REMOVE IN PRODUCTION)
            int currentCode = twoFactorAuthService.getCurrentCode(username);
            log.info("DEBUG - Current expected code for {}: {}", username, currentCode);
            
            return "2fa-setup-page";
        } catch (Exception e) {
            log.error("Error generating 2FA setup for user {}: {}", username, e.getMessage(), e);
            model.addAttribute("error", "Failed to generate 2FA setup: " + e.getMessage());
            return "error-page";
        }
    }

    /**
     * Verification page - Enter 6-digit code (shown after login)
     */
    @GetMapping("/verify")
    public String showVerifyPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("2FA_USERNAME");
        Boolean pending = (Boolean) session.getAttribute("2FA_PENDING");
        
        log.info("=== 2FA Verify page accessed - username: {}, pending: {} ===", username, pending);
        
        if (username == null || pending == null || !pending) {
            log.warn("No valid 2FA session found, redirecting to login");
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        
        // DEBUG: Show current expected code (REMOVE IN PRODUCTION)
        int currentCode = twoFactorAuthService.getCurrentCode(username);
        log.info("DEBUG - Current expected code for {}: {}", username, currentCode);
        
        return "2fa-verify-page";
    }

    /**
     * Handle code verification (after login)
     */
    @PostMapping("/verify")
    public String verifyCode(@RequestParam("code") int code, 
                            HttpSession session, 
                            Model model) {
        String username = (String) session.getAttribute("2FA_USERNAME");
        Boolean pending = (Boolean) session.getAttribute("2FA_PENDING");
        
        log.info("=== 2FA verification attempt - username: {}, code: {}, pending: {} ===", 
            username, code, pending);
        
        if (username == null || pending == null || !pending) {
            log.warn("No valid 2FA session found during verification");
            return "redirect:/login";
        }

        log.info("Verifying 2FA code {} for user: {}", code, username);

        if (twoFactorAuthService.verifyCode(username, code)) {
            log.info("✓ 2FA verification SUCCESSFUL for user: {}", username);
            
            // Get the pre-authentication object
            Authentication preAuth = (Authentication) session.getAttribute("PRE_AUTH");
            
            if (preAuth != null) {
                // Complete the authentication
                SecurityContextHolder.getContext().setAuthentication(preAuth);
                log.info("Authentication completed in SecurityContext for user: {}", username);
            }
            
            // Mark as verified and clear pending flag
            session.removeAttribute("2FA_USERNAME");
            session.removeAttribute("2FA_PENDING");
            session.removeAttribute("PRE_AUTH");
            session.setAttribute("2FA_VERIFIED", true);
            
            log.info("Session updated: 2FA_VERIFIED=true, proceeding to main page");
            return "redirect:/crud/sertifikati/show/all";
        } else {
            log.warn("✗ Invalid 2FA code for user: {} (code was: {})", username, code);
            
            model.addAttribute("error", "Invalid code. Please try again.");
            model.addAttribute("username", username);
            
            // DEBUG: Show expected code (REMOVE IN PRODUCTION)
            int expectedCode = twoFactorAuthService.getCurrentCode(username);
            log.info("DEBUG - Expected code was: {}, received: {}", expectedCode, code);
            
            return "2fa-verify-page";
        }
    }

    /**
     * Enable 2FA - for users who completed setup
     */
    @PostMapping("/enable")
    public String enable2FA(@RequestParam("code") int code,
                           Authentication authentication,
                           Model model) {
        if (authentication == null) {
            log.error("No authentication found when trying to enable 2FA");
            return "redirect:/login";
        }

        String username = authentication.getName();
        log.info("=== User {} attempting to enable 2FA with code: {} ===", username, code);

        MyUser user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found: {}", username);
            model.addAttribute("error", "User not found");
            return "error-page";
        }

        log.info("Current user state - 2FA Enabled: {}, Has Secret: {}", 
            user.isTwoFactorEnabled(), 
            user.getSecretKey() != null && !user.getSecretKey().isEmpty());

        // Verify the code first
        if (twoFactorAuthService.verifyCode(username, code)) {
            log.info("✓ Code verified successfully, enabling 2FA for user: {}", username);
            
            // Enable 2FA
            user.setTwoFactorEnabled(true);
            userRepo.save(user);
            
            // Verify it was saved
            MyUser verifyUser = userRepo.findByUsername(username);
            log.info("2FA ENABLED successfully for user: {} - Flag is now: {}", 
                username, verifyUser.isTwoFactorEnabled());
            
            return "2fa-success-page";
        } else {
            log.warn("✗ Invalid verification code during 2FA enable for user: {} (code: {})", 
                username, code);
            
            // DEBUG: Show expected code
            int expectedCode = twoFactorAuthService.getCurrentCode(username);
            log.info("DEBUG - Expected code was: {}, received: {}", expectedCode, code);
            
            model.addAttribute("error", "Invalid code. Please try again.");
            return "redirect:/2fa/setup?error=invalid";
        }
    }

    /**
     * Disable 2FA
     */
    @PostMapping("/disable")
    public String disable2FA(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        log.info("User {} disabling 2FA", username);

        twoFactorAuthService.disable2FA(username);
        
        model.addAttribute("message", "2FA has been disabled");
        return "redirect:/crud/sertifikati/show/all";
    }
    
    /**
     * Debug endpoint to check 2FA status (REMOVE IN PRODUCTION)
     */
    @GetMapping("/debug")
    public String debug2FA(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        
        String username = authentication.getName();
        MyUser user = userRepo.findByUsername(username);
        
        if (user == null) {
            model.addAttribute("debugInfo", "User not found");
            return "2fa-debug-page";
        }
        
        int currentCode = twoFactorAuthService.getCurrentCode(username);
        long currentTimeWindow = System.currentTimeMillis() / 30000L;
        
        String debugInfo = String.format(
            "Username: %s\n" +
            "2FA Enabled: %s\n" +
            "Has Secret Key: %s\n" +
            "Secret Key (first 10 chars): %s...\n" +
            "\n=== CURRENT EXPECTED CODE ===\n" +
            "Code to enter NOW: %06d\n" +
            "================================\n" +
            "\nTime Window: %d\n" +
            "Time until next code: ~%d seconds",
            username,
            user.isTwoFactorEnabled(),
            user.getSecretKey() != null && !user.getSecretKey().isEmpty(),
            user.getSecretKey() != null ? user.getSecretKey().substring(0, Math.min(10, user.getSecretKey().length())) : "null",
            currentCode,
            currentTimeWindow,
            30 - (int)((System.currentTimeMillis() / 1000) % 30)
        );
        
        model.addAttribute("debugInfo", debugInfo);
        model.addAttribute("currentCode", String.format("%06d", currentCode));
        model.addAttribute("username", username);
        return "2fa-debug-page";
    }
}