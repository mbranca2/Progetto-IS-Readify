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

@WebServlet("/elimina-recensione")
public class EliminaRecensioneServlet extends HttpServlet {

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

        boolean success = reviewService.deleteReview(idRecensione, utente.getIdUtente());

        if (success) {
            session.setAttribute("message", "Recensione eliminata con successo");
        } else {
            session.setAttribute("error", "Errore nell'eliminazione della recensione");
        }

        response.sendRedirect(request.getContextPath() + "/dettaglio-libro?id=" + idLibro);
    }
}