package controller;

import model.Libro;
import model.Recensione;
import model.dao.LibroDAO;
import model.dao.RecensioneDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/libro")
public class DettaglioLibroServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(DettaglioLibroServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idStr = req.getParameter("id");
            LOGGER.log(Level.INFO, "Richiesta dettaglio libro con ID: {0}", idStr);

            int id;
            try {
                id = Integer.parseInt(idStr);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "ID libro non valido: {0}", idStr);
                resp.sendRedirect("libri");
                return;
            }

            LibroDAO libroDAO = new LibroDAO();
            Libro libro = libroDAO.trovaLibroPerId(id);

            if (libro == null) {
                LOGGER.log(Level.WARNING, "Libro non trovato per ID: {0}", id);
                resp.sendRedirect("libri");
                return;
            }

            // recupero tutte le recensioni relative a id libro
            try {
                RecensioneDAO recensioneDAO = new RecensioneDAO();
                List<Recensione> recensioni = recensioneDAO.trovaRecensioniPerLibro(id);
                LOGGER.log(Level.INFO, "Trovate {0} recensioni per il libro ID: {1}", 
                    new Object[]{recensioni.size(), id});

                req.setAttribute("libro", libro);
                req.setAttribute("valutazioni", recensioni);
                req.getRequestDispatcher("jsp/dettaglioLibro.jsp").forward(req, resp);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Errore durante il caricamento delle recensioni", e);
                throw new ServletException("Errore durante il caricamento delle recensioni", e);
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Errore nella servlet DettaglioLibro", e);
            throw new ServletException("Errore durante il recupero dei dettagli del libro", e);
        }
    }
}
