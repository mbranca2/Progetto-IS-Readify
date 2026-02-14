package presentation.controller;

import business.model.Utente;
import business.service.ServiceFactory;
import business.service.review.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static presentation.util.ServletUtils.parseIntSafe;

@WebServlet("/modifica-recensione")
public class ModificaRecensioneServlet extends HttpServlet {

    private ReviewService reviewService = ServiceFactory.reviewService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente utente = (Utente) session.getAttribute("utente");
        int idRecensione = parseIntSafe(request.getParameter("idRecensione"));
        int idLibro = parseIntSafe(request.getParameter("idLibro"));
        int voto = parseIntSafe(request.getParameter("voto"));
        String commento = request.getParameter("commento");

        if (voto < 1 || voto > 5) {
            session.setAttribute("error", "Voto non valido");
            response.sendRedirect(request.getContextPath() + "/dettaglio-libro?id=" + idLibro);
            return;
        }

        boolean success = reviewService.updateReview(idRecensione, utente.getIdUtente(), voto, commento);

        if (success) {
            session.setAttribute("message", "Recensione modificata con successo");
        } else {
            session.setAttribute("error", "Errore nella modifica della recensione");
        }

        response.sendRedirect(request.getContextPath() + "/dettaglio-libro?id=" + idLibro);
    }
}