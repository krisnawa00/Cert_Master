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
import lv.venta.service.impl.TwoFactorAuthService;

@Slf4j
@Controller
@RequestMapping("/2fa")
public class TwoFactorAuthController {

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

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
            // Generate new secret key
            String secret = twoFactorAuthService.generateSecretKey(username);
            
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