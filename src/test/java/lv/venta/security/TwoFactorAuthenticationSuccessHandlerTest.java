package lv.venta.security;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lv.venta.service.impl.TwoFactorAuthService;

class TwoFactorAuthenticationSuccessHandlerTest {

    @Test
    void onAuthenticationSuccess_when2FAEnabled_setsSessionAttributes_andRedirectsToVerify() throws Exception {
        // Arrange
        TwoFactorAuthService twoFactorAuthService = mock(TwoFactorAuthService.class);
        TwoFactorAuthenticationSuccessHandler handler = new TwoFactorAuthenticationSuccessHandler();
        ReflectionTestUtils.setField(handler, "twoFactorAuthService", twoFactorAuthService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("user1");
        when(request.getSession()).thenReturn(session);
        when(twoFactorAuthService.is2FAEnabled("user1")).thenReturn(true);

        // Act
        handler.onAuthenticationSuccess(request, response, authentication);

        // Assert: session attributes set
        verify(session).setAttribute("2FA_USERNAME", "user1");
        verify(session).setAttribute("2FA_PENDING", true);
        verify(session).setAttribute("2FA_VERIFIED", false);
        verify(session).setAttribute("PRE_AUTH", authentication);

        // Assert: redirect
        verify(response).sendRedirect("/2fa/verify");

        // Assert: no "no-2FA" actions
        verify(session, never()).removeAttribute("2FA_PENDING");
        verify(response, never()).sendRedirect("/crud/sertifikati/show/all");
    }

    @Test
    void onAuthenticationSuccess_when2FADisabled_marksVerified_clearsPending_andRedirectsToMain() throws Exception {
        // Arrange
        TwoFactorAuthService twoFactorAuthService = mock(TwoFactorAuthService.class);
        TwoFactorAuthenticationSuccessHandler handler = new TwoFactorAuthenticationSuccessHandler();
        ReflectionTestUtils.setField(handler, "twoFactorAuthService", twoFactorAuthService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Authentication authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn("user2");
        when(request.getSession()).thenReturn(session);
        when(twoFactorAuthService.is2FAEnabled("user2")).thenReturn(false);

        // Act
        handler.onAuthenticationSuccess(request, response, authentication);

        // Assert: verified + pending removed
        verify(session).setAttribute("2FA_VERIFIED", true);
        verify(session).removeAttribute("2FA_PENDING");

        // Assert: redirect
        verify(response).sendRedirect("/crud/sertifikati/show/all");

        // Assert: no 2FA-enabled flow attributes
        verify(session, never()).setAttribute(eq("2FA_USERNAME"), any());
        verify(session, never()).setAttribute(eq("PRE_AUTH"), any());
        verify(response, never()).sendRedirect("/2fa/verify");
    }
}
