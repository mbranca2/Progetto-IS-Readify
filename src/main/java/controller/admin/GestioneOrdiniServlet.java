package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.Utente;
import model.dao.OrdineDAO;

import java.io.IOException;

@WebServlet("/admin/ordini")
public class GestioneOrdiniServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Utente utente = (Utente) session.getAttribute("utente");
        if (!"admin".equals(utente.getRuolo())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        OrdineDAO ordineDAO = new OrdineDAO();
        req.setAttribute("ordini", ordineDAO.trovaTuttiOrdini());

        req.getRequestDispatcher("/WEB-INF/jsp/admin/gestione-ordini.jsp").forward(req, resp);
    }
}
