package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.*;
import model.dao.IndirizzoDAO;
import model.dao.OrdineDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/salva-ordine")
public class SalvaOrdineServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(SalvaOrdineServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute("utente");
        Carrello carrello = (Carrello) session.getAttribute("carrello");

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (carrello == null || carrello.getArticoli().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il carrello è vuoto");
            return;
        }

        try {
            Ordine ordine = new Ordine();
            ordine.setIdUtente(utente.getIdUtente());
            ordine.setStato(StatoOrdine.IN_ELABORAZIONE);
            ordine.setTotale(carrello.getTotale());

            IndirizzoDAO indirizzoDAO = new IndirizzoDAO();
            Indirizzo indirizzo = indirizzoDAO.trovaIndirizzoPerId(utente.getIdUtente());
            if (indirizzo != null) {
                ordine.setIdIndirizzo(indirizzo.getIdIndirizzo());
            }

            for (Carrello.ArticoloCarrello articolo : carrello.getArticoli()) {
                DettaglioOrdine dettaglio = new DettaglioOrdine();
                dettaglio.setIdLibro(articolo.getLibro().getIdLibro());
                dettaglio.setQuantita(articolo.getQuantita());
                dettaglio.setPrezzoUnitario(articolo.getLibro().getPrezzo());
                dettaglio.setTitoloLibro(articolo.getLibro().getTitolo());
                dettaglio.setAutoreLibro(articolo.getLibro().getAutore());
                dettaglio.setIsbnLibro(articolo.getLibro().getIsbn());
                dettaglio.setImmagineCopertina(articolo.getLibro().getCopertina());
                ordine.aggiungiDettaglio(dettaglio);
            }

            OrdineDAO ordineDAO = new OrdineDAO();
            boolean successo = ordineDAO.salvaOrdine(ordine);
            if (successo) {
                carrello.svuota();
                session.setAttribute("carrello", carrello);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": true, \"orderId\": " + ordine.getIdOrdine() + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"error\": \"Errore nel salvataggio dell'ordine\"}");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante il salvataggio dell'ordine", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"error\": \"Errore di sistema\"}");
        }
    }
}
