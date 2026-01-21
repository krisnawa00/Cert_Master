package lv.venta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
        log.info("User {} accessing 2FA setup page", username);

        try {
            // Check if user already has a secret
            MyUser user = userRepo.findByUsername(username);
            String secret;
            
            if (user.getSecretKey() == null || user.getSecretKey().isEmpty()) {
                // Generate new secret key and SAVE IT
                secret = twoFactorAuthService.generateSecretKey(username);
                log.info("Generated new secret key for user: {}", username);
            } else {
                // Use existing secret
                secret = user.getSecretKey();
                log.info("Using existing secret key for user: {}", username);
            }
            
            // Generate QR code image
            String qrCodeImage = twoFactorAuthService.generateQRCodeImage(username, secret);
            
            model.addAttribute("username", username);
            model.addAttribute("qrCode", qrCodeImage);
            model.addAttribute("secretKey", secret);
            
            return "2fa-setup-page";
        } catch (Exception e) {
            log.error("Error generating 2FA setup for user {}: {}", username, e.getMessage(), e);
            model.addAttribute("error", "Failed to generate 2FA setup: " + e.getMessage());
            return "error-page";
        }
    }

    /**
     * Verification page - Enter 6-digit code
     */
    @GetMapping("/verify")
    public String showVerifyPage(HttpSession session, Model model) {
        String username = (String) session.getAttribute("2FA_USERNAME");
        
        if (username == null) {
            log.warn("No username in session for 2FA verification");
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        return "2fa-verify-page";
    }

    /**
     * Handle code verification
     */
    @PostMapping("/verify")
    public String verifyCode(@RequestParam("code") int code, 
                            HttpSession session, 
                            Model model) {
        String username = (String) session.getAttribute("2FA_USERNAME");
        
        if (username == null) {
            log.warn("No username in session during 2FA verification");
            return "redirect:/login";
        }

        log.info("Verifying 2FA code for user: {}", username);

        if (twoFactorAuthService.verifyCode(username, code)) {
            // Code is valid - complete login
            session.removeAttribute("2FA_USERNAME");
            session.setAttribute("2FA_VERIFIED", true);
            
            log.info("2FA verification successful for user: {}", username);
            return "redirect:/crud/sertifikati/show/all";
        } else {
            // Code is invalid
            log.warn("Invalid 2FA code for user: {}", username);
            model.addAttribute("error", "Invalid code. Please try again.");
            model.addAttribute("username", username);
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
        log.info("User {} attempting to enable 2FA with code: {}", username, code);

        MyUser user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found: {}", username);
            model.addAttribute("error", "User not found");
            return "error-page";
        }

        log.info("Current user state - 2FA Enabled: {}, Secret Key: {}", 
            user.isTwoFactorEnabled(), 
            user.getSecretKey() != null ? "SET" : "NULL");

        // Verify the code first
        if (twoFactorAuthService.verifyCode(username, code)) {
            // Enable 2FA
            user.setTwoFactorEnabled(true);
            userRepo.save(user);
            
            log.info("2FA ENABLED successfully for user: {} - Flag is now: {}", 
                username, user.isTwoFactorEnabled());
            
            // Show success page
            return "2fa-success-page";
        } else {
            log.warn("Invalid verification code during 2FA enable for user: {} (code was: {})", username, code);
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
}