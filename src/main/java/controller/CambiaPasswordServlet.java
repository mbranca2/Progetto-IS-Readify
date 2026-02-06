package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.Utente;
import service.ServiceFactory;
import service.account.AccountService;

import java.io.IOException;

@WebServlet("/cambia-password")
public class CambiaPasswordServlet extends HttpServlet {

    private AccountService accountService;

    @Override
    public void init() throws ServletException {
        this.accountService = ServiceFactory.accountService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("errore", "Le nuove password non coincidono.");
            request.getRequestDispatcher("/WEB-INF/jsp/profilo.jsp").forward(request, response);
            return;
        }

        boolean ok = accountService.changePassword(utente.getIdUtente(), oldPassword, newPassword);

        if (ok) {
            request.setAttribute("successo", "Password aggiornata con successo.");
        } else {
            request.setAttribute("errore", "Impossibile aggiornare la password. Verifica la password attuale.");
        }

        request.getRequestDispatcher("/WEB-INF/jsp/profilo.jsp").forward(request, response);
    }
}
