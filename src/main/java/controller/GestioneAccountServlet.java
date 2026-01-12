package controller;

import model.Utente;
import model.dao.UtenteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/profilo")
public class GestioneAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            resp.sendRedirect("jsp/login.jsp");
            return;
        }
        // mostro pagina dati account
        req.getRequestDispatcher("jsp/gestioneAccount.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            resp.sendRedirect("jsp/login.jsp");
            return;
        }
        Utente utente = (Utente) session.getAttribute("utente");
        // Recupero i dati dal form
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String email = req.getParameter("email");
        String telefono = req.getParameter("telefono");
        // Validazione server-side
        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                email == null || email.trim().isEmpty()) {
            req.setAttribute("errore", "Tutti i campi obbligatori devono essere compilati");
            req.getRequestDispatcher("jsp/gestioneAccount.jsp").forward(req, resp);
            return;
        }
        // Aggiorno i dati
        utente.setNome(nome.trim());
        utente.setCognome(cognome.trim());
        utente.setEmail(email.trim().toLowerCase());
        utente.setTelefono(telefono != null ? telefono.trim() : null);
        // salvo in db
        UtenteDAO utenteDAO = new UtenteDAO();
        boolean aggiornato = utenteDAO.aggiornaUtente(utente);
        if (aggiornato) {
            session.setAttribute("utente", utente);
            req.setAttribute("successo", "Profilo aggiornato con successo");
        } else {
            req.setAttribute("errore", "Si è verificato un errore durante l'aggiornamento del profilo");
        }
        req.getRequestDispatcher("jsp/gestioneAccount.jsp").forward(req, resp);
    }
}
