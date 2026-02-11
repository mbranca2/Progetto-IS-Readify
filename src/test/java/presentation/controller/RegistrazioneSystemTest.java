package presentation.controller;

import business.service.account.AccountService;
import business.service.account.RegistrationData;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testUtil.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistrazioneSystemTest {
    private static final String REGISTER_JSP = "/WEB-INF/jsp/register.jsp";
    @Test
    @DisplayName("TC1.1.1 Registrazione: password non valida")
    void tcRegistrazione_passwordNonValida_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        stubValidRegistrationParams(req);

        when(req.getParameter("password")).thenReturn("Rosarossa");
        when(req.getParameter("confermaPassword")).thenReturn("Rosarossa");
        when(req.getRequestDispatcher(REGISTER_JSP)).thenReturn(rd);
        servlet.doPost(req, resp);
        assertInvalidRegistrationForward(req, resp, rd);
        verify(accountService, never()).register((RegistrationData) any());
    }

    @Test
    @DisplayName("TC1.1.2 Registrazione: conferma password diversa")
    void tcRegistrazione_confermaNonCoincide_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        stubValidRegistrationParams(req);
        when(req.getParameter("password")).thenReturn("Rosarossa3");
        when(req.getParameter("confermaPassword")).thenReturn("Rosarossa5");
        when(req.getRequestDispatcher(REGISTER_JSP)).thenReturn(rd);
        servlet.doPost(req, resp);
        assertInvalidRegistrationForward(req, resp, rd);
        verify(accountService, never()).register((RegistrationData) any());
    }

    @Test
    @DisplayName("TC1.1.3 Registrazione: email non valida")
    void tcRegistrazione_emailNonValida_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        stubValidRegistrationParams(req);

        when(req.getParameter("email")).thenReturn("robertorossi103gmail.com");
        when(req.getRequestDispatcher(REGISTER_JSP)).thenReturn(rd);
        servlet.doPost(req, resp);
        assertInvalidRegistrationForward(req, resp, rd);
        verify(accountService, never()).register((RegistrationData) any());
    }

    @Test
    @DisplayName("TC1.1.4 Registrazione: CAP non valido")
    void tcRegistrazione_capNonValido_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        stubValidRegistrationParams(req);

        when(req.getParameter("cap")).thenReturn("ABCDE");
        when(req.getRequestDispatcher(REGISTER_JSP)).thenReturn(rd);
        servlet.doPost(req, resp);
        assertInvalidRegistrationForward(req, resp, rd);
        verify(accountService, never()).register((RegistrationData) any());
    }

    @Test
    @DisplayName("TC1.1.5 Registrazione: provincia non valida")
    void tcRegistrazione_provinciaNonValida_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        stubValidRegistrationParams(req);

        when(req.getParameter("provincia")).thenReturn("M1");
        when(req.getRequestDispatcher(REGISTER_JSP)).thenReturn(rd);
        servlet.doPost(req, resp);
        assertInvalidRegistrationForward(req, resp, rd);
        verify(accountService, never()).register((RegistrationData) any());
    }

    @Test
    @DisplayName("TC1.1.6 Registrazione: telefono non valido")
    void tcRegistrazione_telefonoNonValido_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        stubValidRegistrationParams(req);

        when(req.getParameter("telefono")).thenReturn("ABC");
        when(req.getRequestDispatcher(REGISTER_JSP)).thenReturn(rd);
        servlet.doPost(req, resp);
        assertInvalidRegistrationForward(req, resp, rd);
        verify(accountService, never()).register((RegistrationData) any());
    }

    @Test
    @DisplayName("TC1.1.7 Registrazione: completata")
    void tcRegistrazione_successo_redirect() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getContextPath()).thenReturn("");
        stubValidRegistrationParams(req);
        servlet.doPost(req, resp);
        verify(resp, times(1)).sendRedirect("/login?registrazione=successo");
        verify(accountService, times(1)).register((RegistrationData) any());
    }

    private void stubValidRegistrationParams(HttpServletRequest req) {
        when(req.getParameter("nome")).thenReturn("Roberto");
        when(req.getParameter("cognome")).thenReturn("Rossi");
        when(req.getParameter("email")).thenReturn("robertorossi103@gmail.com");
        when(req.getParameter("telefono")).thenReturn("3913435888");
        when(req.getParameter("via")).thenReturn("Via Alberto Sordi 212");
        when(req.getParameter("citta")).thenReturn("Milano");
        when(req.getParameter("cap")).thenReturn("20019");
        when(req.getParameter("provincia")).thenReturn("MI");
        when(req.getParameter("paese")).thenReturn("Italia");
        when(req.getParameter("password")).thenReturn("Rosarossa3");
        when(req.getParameter("confermaPassword")).thenReturn("Rosarossa3");
    }

    private void assertInvalidRegistrationForward(HttpServletRequest req,
                                                  HttpServletResponse resp,
                                                  RequestDispatcher rd) throws Exception {
        verify(req).setAttribute(eq("nome"), any());
        verify(req).setAttribute(eq("cognome"), any());
        verify(req).setAttribute(eq("email"), any());
        verify(req).setAttribute(eq("telefono"), any());
        verify(req).setAttribute(eq("errori"), any());
        verify(rd).forward(req, resp);
    }
}
