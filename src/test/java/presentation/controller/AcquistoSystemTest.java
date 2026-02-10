package presentation.controller;

import business.model.Carrello;
import business.model.Ordine;
import business.model.Utente;
import business.service.account.AccountService;
import business.service.address.AddressService;
import business.service.order.OrderService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testUtil.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// Classe per acquisto.
class AcquistoSystemTest {

    @Test
    @DisplayName("TC2.1.1 Acquisto: CAP non valido -> forward pagamento.jsp con errore")
    void tcIndirizzo_capNonValido() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdPagamento = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubNewAddressParams(req, "0004223", "RM");
        stubPaymentParams(req, "5767991234001156", "12/30", "344");

        when(addressService.listByUser(1)).thenReturn(Collections.emptyList());
        when(req.getRequestDispatcher("/WEB-INF/jsp/pagamento.jsp")).thenReturn(rdPagamento);

        servlet.doPost(req, resp);

        assertErroreSet(req);
    }

    @Test
    @DisplayName("TC2.1.2 Acquisto: provincia non valida -> forward pagamento.jsp con errore")
    void tcIndirizzo_provinciaNonValida() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdPagamento = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubNewAddressParams(req, "00042", "RMA");
        stubPaymentParams(req, "5767991234001156", "12/30", "344");

        when(addressService.listByUser(1)).thenReturn(Collections.emptyList());
        when(req.getRequestDispatcher("/WEB-INF/jsp/pagamento.jsp")).thenReturn(rdPagamento);

        servlet.doPost(req, resp);

        assertErroreSet(req);
    }

    @Test
    @DisplayName("TC2.1.3 Acquisto: indirizzo valido -> indirizzo salvato, forward pagamento.jsp")
    void tcIndirizzo_valido() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdPagamento = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubNewAddressParams(req, "00042", "RM");
        stubPaymentParams(req, "5767991234001156669", "12/30", "344");

        when(accountService.addAddress(eq(1), any())).thenReturn(true);
        when(addressService.listByUser(1)).thenReturn(Collections.emptyList());
        when(req.getRequestDispatcher("/WEB-INF/jsp/pagamento.jsp")).thenReturn(rdPagamento);

        servlet.doPost(req, resp);

        assertAddressSaved(accountService);
    }

    @Test
    @DisplayName("TC2.2.1 Pagamento: numero carta non valido -> forward pagamento.jsp con errore")
    void tcPagamento_numeroCartaNonValido() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdPagamento = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubExistingAddress(req, addressService);
        stubPaymentParams(req, "5767991234001156669", "12/30", "344");

        when(req.getRequestDispatcher("/WEB-INF/jsp/pagamento.jsp")).thenReturn(rdPagamento);

        servlet.doPost(req, resp);

        assertErroreSet(req);
    }

    @Test
    @DisplayName("TC2.2.2 Pagamento: scadenza non valida -> forward pagamento.jsp con errore")
    void tcPagamento_scadenzaNonValida() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdPagamento = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubExistingAddress(req, addressService);
        stubPaymentParams(req, "5767991234001156", "15/30", "344");

        when(req.getRequestDispatcher("/WEB-INF/jsp/pagamento.jsp")).thenReturn(rdPagamento);

        servlet.doPost(req, resp);

        assertErroreSet(req);
    }

    @Test
    @DisplayName("TC2.2.3 Pagamento: scadenza passata -> forward pagamento.jsp con errore")
    void tcPagamento_scadenzaPassata() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdPagamento = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubExistingAddress(req, addressService);
        stubPaymentParams(req, "5767991234001156", "12/19", "344");

        when(req.getRequestDispatcher("/WEB-INF/jsp/pagamento.jsp")).thenReturn(rdPagamento);

        servlet.doPost(req, resp);

        assertErroreSet(req);
    }

    @Test
    @DisplayName("TC2.2.4 Pagamento: CVV non valido -> forward pagamento.jsp con errore")
    void tcPagamento_cvvNonValido() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdPagamento = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubExistingAddress(req, addressService);
        stubPaymentParams(req, "5767991234001156", "12/30", "3445");

        when(req.getRequestDispatcher("/WEB-INF/jsp/pagamento.jsp")).thenReturn(rdPagamento);

        servlet.doPost(req, resp);

        assertErroreSet(req);
    }

    @Test
    @DisplayName("TC2.2.5 Pagamento: successo -> forward conferma-ordine.jsp, carrello svuotato, idOrdine settato")
    void tcPagamento_successo() throws Exception {
        ConfermaOrdineServlet servlet = new ConfermaOrdineServlet();

        OrderService orderService = mock(OrderService.class);
        AddressService addressService = mock(AddressService.class);
        AccountService accountService = mock(AccountService.class);

        ReflectionTestUtils.setField(servlet, "orderService", orderService);
        ReflectionTestUtils.setField(servlet, "addressService", addressService);
        ReflectionTestUtils.setField(servlet, "accountService", accountService);

        Ordine ordine = new Ordine();
        ordine.setIdOrdine(99);

        when(orderService.placeOrder(eq(1), eq(10), any())).thenReturn(ordine);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher rdConferma = mock(RequestDispatcher.class);

        Utente u = new Utente();
        u.setIdUtente(1);

        Carrello c = mock(Carrello.class);
        when(c.isVuoto()).thenReturn(false);

        stubSession(req, session, u, c);
        stubExistingAddress(req, addressService);
        stubPaymentParams(req, "5767991234001156", "12/30", "344");

        when(req.getRequestDispatcher("/WEB-INF/jsp/conferma-ordine.jsp")).thenReturn(rdConferma);

        servlet.doPost(req, resp);

        assertIdOrdineSet(req);
    }

    private void stubSession(HttpServletRequest req, HttpSession session, Utente u, Carrello c) {
        when(req.getSession(false)).thenReturn(session);
        when(session.getAttribute("utente")).thenReturn(u);
        when(session.getAttribute("carrello")).thenReturn(c);
    }

    private void stubExistingAddress(HttpServletRequest req, AddressService addressService) {
        when(req.getParameter("indirizzoSpedizione")).thenReturn("10");
        when(addressService.isOwnedByUser(10, 1)).thenReturn(true);
    }

    private void stubNewAddressParams(HttpServletRequest req, String cap, String provincia) {
        when(req.getParameter("indirizzoSpedizione")).thenReturn("new");
        when(req.getParameter("via")).thenReturn("Via Patria Maggiore 96");
        when(req.getParameter("citta")).thenReturn("Roma");
        when(req.getParameter("cap")).thenReturn(cap);
        when(req.getParameter("provincia")).thenReturn(provincia);
        when(req.getParameter("paese")).thenReturn("Italia");
    }

    private void stubPaymentParams(HttpServletRequest req, String cardNumber, String expiryDate, String cvv) {
        when(req.getParameter("cardName")).thenReturn("Roberto Rossi");
        when(req.getParameter("cardNumber")).thenReturn(cardNumber);
        when(req.getParameter("expiryDate")).thenReturn(expiryDate);
        when(req.getParameter("cvv")).thenReturn(cvv);
    }

    private void assertErroreSet(HttpServletRequest req) {
        verify(req).setAttribute(eq("errore"), anyString());
    }

    private void assertIdOrdineSet(HttpServletRequest req) {
        verify(req).setAttribute(eq("idOrdine"), eq(99));
    }

    private void assertAddressSaved(AccountService accountService) {
        verify(accountService, times(1)).addAddress(eq(1), any());
    }
}