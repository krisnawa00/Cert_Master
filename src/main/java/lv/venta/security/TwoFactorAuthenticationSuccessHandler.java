package lv.venta.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
        log.info("Authentication successful for user: {}", username);
        
        HttpSession session = request.getSession();
        
        // Check if user has 2FA enabled
        if (twoFactorAuthService.is2FAEnabled(username)) {
            log.info("User {} has 2FA enabled, redirecting to verification", username);
            
            // Store username in session for verification
            session.setAttribute("2FA_USERNAME", username);
            session.setAttribute("2FA_VERIFIED", false);
            
            // Redirect to 2FA verification page
            response.sendRedirect("/2fa/verify");
        } else {
            log.info("User {} does not have 2FA enabled, proceeding to main page", username);
            
            // No 2FA - proceed to default success URL
            response.sendRedirect("/crud/sertifikati/show/all");
        }
    }
}