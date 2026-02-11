package presentation.controller;

import business.model.Carrello;
import business.model.Utente;
import business.service.account.AccountService;
import business.service.cart.CartFacade;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testUtil.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoginSystemTest {

    private static final String LOGIN_JSP = "/WEB-INF/jsp/login.jsp";

    @Test
    @DisplayName("TC1.2.1 Login: utente non trovato")
    void tcLogin_emailNonRegistrata() throws Exception {
        LoginServlet servlet = new LoginServlet();
        AccountService accountService = mock(AccountService.class);
        CartFacade cartFacade = mock(CartFacade.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);
        ReflectionTestUtils.setField(servlet, "cartFacade", cartFacade);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        when(req.getParameter("email")).thenReturn("robertorossi100@gmail.com");
        when(req.getParameter("password")).thenReturn("Rosarossa3");
        when(req.getContextPath()).thenReturn("");
        when(req.getRequestDispatcher(LOGIN_JSP)).thenReturn(rd);
        when(accountService.login(eq("robertorossi100@gmail.com"), eq("Rosarossa3"))).thenReturn(null);
        servlet.doPost(req, resp);
        verify(req).setAttribute(eq("error"), anyString());
        verify(req).setAttribute(eq("email"), anyString());
        verify(rd).forward(req, resp);
        verify(cartFacade, never()).syncAfterLogin(anyInt(), any());
    }

    @Test
    @DisplayName("TC1.2.2 Login: email non valida")
    void tcLogin_emailNonValida() throws Exception {
        LoginServlet servlet = new LoginServlet();
        AccountService accountService = mock(AccountService.class);
        CartFacade cartFacade = mock(CartFacade.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);
        ReflectionTestUtils.setField(servlet, "cartFacade", cartFacade);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        when(req.getParameter("email")).thenReturn("robertorossi103gmail.com");
        when(req.getParameter("password")).thenReturn("Rosarossa3");
        when(req.getRequestDispatcher(LOGIN_JSP)).thenReturn(rd);
        servlet.doPost(req, resp);
        verify(req).setAttribute(eq("errori"), any());
        verify(req).setAttribute(eq("email"), anyString());
        verify(rd).forward(req, resp);
        verify(accountService, never()).login(anyString(), anyString());
        verify(cartFacade, never()).syncAfterLogin(anyInt(), any());
    }

    @Test
    @DisplayName("TC1.2.3 Login: credenziali errate")
    void tcLogin_passwordErrata() throws Exception {
        LoginServlet servlet = new LoginServlet();
        AccountService accountService = mock(AccountService.class);
        CartFacade cartFacade = mock(CartFacade.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);
        ReflectionTestUtils.setField(servlet, "cartFacade", cartFacade);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        RequestDispatcher rd = mock(RequestDispatcher.class);

        when(req.getParameter("email")).thenReturn("robertorossi103@gmail.com");
        when(req.getParameter("password")).thenReturn("Rosarossa5");
        when(req.getContextPath()).thenReturn("");
        when(req.getRequestDispatcher(LOGIN_JSP)).thenReturn(rd);
        when(accountService.login(eq("robertorossi103@gmail.com"), eq("Rosarossa5"))).thenReturn(null);
        servlet.doPost(req, resp);
        verify(req).setAttribute(eq("error"), anyString());
        verify(req).setAttribute(eq("email"), anyString());
        verify(rd).forward(req, resp);
        verify(cartFacade, never()).syncAfterLogin(anyInt(), any());
    }

    @Test
    @DisplayName("TC1.2.4 Login: riuscito")
    void tcLogin_successo() throws Exception {
        LoginServlet servlet = new LoginServlet();
        AccountService accountService = mock(AccountService.class);
        CartFacade cartFacade = mock(CartFacade.class);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);
        ReflectionTestUtils.setField(servlet, "cartFacade", cartFacade);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession newSession = mock(HttpSession.class);

        Utente utente = new Utente();
        utente.setIdUtente(1);
        utente.setRuolo("cliente");

        Carrello carrello = mock(Carrello.class);

        when(req.getParameter("email")).thenReturn("robertorossi103@gmail.com");
        when(req.getParameter("password")).thenReturn("Rosarossa3");
        when(req.getContextPath()).thenReturn("");
        when(req.getSession(false)).thenReturn(null);
        when(req.getSession(true)).thenReturn(newSession);
        when(accountService.login(eq("robertorossi103@gmail.com"), eq("Rosarossa3"))).thenReturn(utente);
        when(cartFacade.syncAfterLogin(eq(1), isNull())).thenReturn(carrello);
        servlet.doPost(req, resp);
        verify(newSession).setAttribute(eq("utente"), eq(utente));
        verify(newSession).setAttribute(eq("idUtente"), eq(1));
        verify(cartFacade, times(1)).syncAfterLogin(eq(1), isNull());
        verify(newSession).setAttribute(eq("carrello"), eq(carrello));
        verify(resp).sendRedirect("/home");
    }
}
