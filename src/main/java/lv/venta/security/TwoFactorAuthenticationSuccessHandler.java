package lv.venta.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import lv.venta.service.impl.TwoFactorAuthService;

import java.io.IOException;

@Slf4j
public class TwoFactorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) 
            throws IOException, ServletException {
        
        String username = authentication.getName();
        log.info("=== Authentication successful for user: {} ===", username);
        
        HttpSession session = request.getSession();
        
        // Check if user has 2FA enabled
        boolean is2FAEnabled = twoFactorAuthService.is2FAEnabled(username);
        log.info("User {} - 2FA enabled: {}", username, is2FAEnabled);
        
        if (is2FAEnabled) {
            log.info("User {} has 2FA enabled, redirecting to verification page", username);
            
            // Store username in session for verification
            session.setAttribute("2FA_USERNAME", username);
            session.setAttribute("2FA_PENDING", true);
            session.setAttribute("2FA_VERIFIED", false);
            
            // Store the authentication temporarily (but don't fully authenticate yet)
            session.setAttribute("PRE_AUTH", authentication);
            
            log.info("Session attributes set: 2FA_USERNAME={}, 2FA_PENDING=true", username);
            
            // Redirect to 2FA verification page
            response.sendRedirect("/2fa/verify");
        } else {
            log.info("User {} does not have 2FA enabled, proceeding to main page", username);
            
            // No 2FA - mark as verified and proceed
            session.setAttribute("2FA_VERIFIED", true);
            session.removeAttribute("2FA_PENDING");
            
            // Proceed to default success URL
            response.sendRedirect("/crud/sertifikati/show/all");
        }
    }
}