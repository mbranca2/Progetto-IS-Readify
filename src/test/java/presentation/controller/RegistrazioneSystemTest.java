package presentation.controller;

import business.service.account.AccountService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testUtil.ReflectionTestUtils;

import static org.mockito.Mockito.*;

// Classe per registrazione.
class RegistrazioneSystemTest {

    @Test
    @DisplayName("TC1.1.1 Registrazione: password non valida -> forward register.jsp + errori")
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

        when(req.getRequestDispatcher("/WEB-INF/jsp/register.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        assertInvalidRegistrationForward(req, resp, rd);
    }

    @Test
    @DisplayName("TC1.1.2 Registrazione: conferma password non coincide -> forward register.jsp + errori")
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

        when(req.getRequestDispatcher("/WEB-INF/jsp/register.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        assertInvalidRegistrationForward(req, resp, rd);
    }

    @Test
    @DisplayName("TC1.1.3 Registrazione: email non valida -> forward register.jsp + errori")
    void tcRegistrazione_emailNonValida_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();

        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        stubValidRegistrationParams(req);
        when(req.getParameter("email")).thenReturn("robertorossi103gmail.com");

        when(req.getRequestDispatcher("/WEB-INF/jsp/register.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        assertInvalidRegistrationForward(req, resp, rd);
    }

    @Test
    @DisplayName("TC1.1.4 Registrazione: CAP non valido -> forward register.jsp + errori")
    void tcRegistrazione_capNonValido_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();

        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        stubValidRegistrationParams(req);
        when(req.getParameter("cap")).thenReturn("2001978");

        when(req.getRequestDispatcher("/WEB-INF/jsp/register.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        assertInvalidRegistrationForward(req, resp, rd);
    }

    @Test
    @DisplayName("TC1.1.5 Registrazione: provincia non valida -> forward register.jsp + errori")
    void tcRegistrazione_provinciaNonValida_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();

        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        stubValidRegistrationParams(req);
        when(req.getParameter("provincia")).thenReturn("MIL");

        when(req.getRequestDispatcher("/WEB-INF/jsp/register.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        assertInvalidRegistrationForward(req, resp, rd);
    }

    @Test
    @DisplayName("TC1.1.6 Registrazione: telefono non valido -> forward register.jsp + errori")
    void tcRegistrazione_telefonoNonValido_forward() throws Exception {
        RegisterServlet servlet = new RegisterServlet();

        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        stubValidRegistrationParams(req);
        when(req.getParameter("telefono")).thenReturn("391343588891");

        when(req.getRequestDispatcher("/WEB-INF/jsp/register.jsp")).thenReturn(rd);

        servlet.doPost(req, resp);

        assertInvalidRegistrationForward(req, resp, rd);
    }

    @Test
    @DisplayName("TC1.1.7 Registrazione: successo -> redirect verso login (registrazione=successo)")
    void tcRegistrazione_successo_redirect() throws Exception {
        RegisterServlet servlet = new RegisterServlet();

        AccountService accountService = mock(AccountService.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getContextPath()).thenReturn("");

        stubValidRegistrationParams(req);
        when(req.getParameter("password")).thenReturn("Rosarossa3");
        when(req.getParameter("confermaPassword")).thenReturn("Rosarossa3");

        servlet.doPost(req, resp);

        assertValidRegistrationRedirect(resp);
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
        verify(rd).forward(req, resp);
    }

    private void assertValidRegistrationRedirect(HttpServletResponse resp) throws Exception {
        verify(resp, times(1)).sendRedirect("/login?registrazione=successo");
    }
}