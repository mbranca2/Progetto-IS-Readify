package controller.admin;

import model.Categoria;
import model.Libro;
import model.dao.CategoriaDAO;
import model.dao.LibroDAO;
import model.Utente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/admin/libri")
public class GestioneLibriServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            resp.sendRedirect("../jsp/login.jsp");
            return;
        }

        Utente utente = (Utente) session.getAttribute("utente");
        if (!"admin".equals(utente.getRuolo())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Gestione dei parametri di ricerca
        String titolo = req.getParameter("titolo");
        String autore = req.getParameter("autore");
        String categoria = req.getParameter("categoria");
        int pagina = (req.getParameter("pagina") != null) ? Integer.parseInt(req.getParameter("pagina")) : 1;
        int elementiPerPagina = 10;
        int offset = (pagina - 1) * elementiPerPagina;

        LibroDAO libroDAO = new LibroDAO();
        CategoriaDAO categoriaDAO = new CategoriaDAO();

        // Recupera tutte le categorie
        List<Categoria> categorie = categoriaDAO.trovaTutteCategorie();
        req.setAttribute("categorie", categorie);

        try {
    // devug        System.out.println("[GestioneLibriServlet] Inizio elaborazione richiesta");
    // debug        System.out.println("[GestioneLibriServlet] Parametri - titolo: " + titolo + ", autore: " + autore + ", categoria: " + categoria);
            
            // Se non ci sono parametri di ricerca, mostra tutti i libri
            if (titolo == null && autore == null && categoria == null) {
                System.out.println("[GestioneLibriServlet] Nessun filtro di ricerca, caricamento di tutti i libri");
                List<Libro> libri = null;
                try {
                    libri = libroDAO.trovaTutti();
                    System.out.println("[GestioneLibriServlet] Libri trovati: " + (libri != null ? libri.size() : "null"));
                    if (libri != null) {
                        for (Libro libro : libri) {
                            System.out.println("[GestioneLibriServlet] Libro: " + libro.getTitolo() + " (ID: " + libro.getIdLibro() + ")");
                        }
                    }
                    req.setAttribute("libri", libri);
                } catch (Exception e) {
                    System.err.println("[GestioneLibriServlet] Errore durante il recupero dei libri: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            } else {
                // Altrimenti filtra i libri
                List<Libro> libri = libroDAO.trovaLibriConFiltro(titolo, autore, categoria, offset, elementiPerPagina);
                System.out.println("[GestioneLibriServlet] Numero libri filtrati: " + (libri != null ? libri.size() : "null"));
                req.setAttribute("libri", libri);

                // Calcola il numero totale di pagine
                int totaleLibri = libroDAO.contaLibriConFiltro(titolo, autore, categoria);
                int totalePagine = (int) Math.ceil((double) totaleLibri / elementiPerPagina);
                req.setAttribute("totalePagine", totalePagine);
                req.setAttribute("paginaCorrente", pagina);
            }

            // Salva i parametri di ricerca nella sessione per mantenere la persistenza
            HttpSession sessione = req.getSession();
            sessione.setAttribute("titolo", titolo);
            sessione.setAttribute("autore", autore);
            sessione.setAttribute("categoria", categoria);

            req.getRequestDispatcher("../jsp/admin/gestione-libri.jsp").forward(req, resp);
        } catch (Exception e) {
            System.err.println("[GestioneLibriServlet] Errore: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Errore nel GestioneLibriServlet: " + e.getMessage(), e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LibroDAO libroDAO = new LibroDAO();

        String azione = request.getParameter("azione");

        if ("aggiungi".equals(azione)) {
            Libro libro = new Libro();
            setLibroPropertiesFromRequest(libro, request);
            libroDAO.inserisciLibro(libro);
            
            // Recupero i parametri di ricerca dalla ssessione
            HttpSession session = request.getSession();
            String titolo = (String) session.getAttribute("titolo");
            String autore = (String) session.getAttribute("autore");
            String categoria = (String) session.getAttribute("categoria");
            
            // Costruisco l'URL con i parametri
            StringBuilder url = new StringBuilder("../admin/libri");
            if (titolo != null || autore != null || categoria != null) {
                url.append("?");
                if (titolo != null) url.append("titolo=").append(titolo);
                if (autore != null) url.append("&autore=").append(autore);
                if (categoria != null) url.append("&categoria=").append(categoria);
            }
            response.sendRedirect(url.toString());

        } else if ("modifica".equals(azione)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Libro libro = libroDAO.trovaLibroPerId(id);
            if (libro != null) {
                setLibroPropertiesFromRequest(libro, request);
                libroDAO.aggiornaLibro(libro);
                
                // Recupero i parametri di ricerca dalla sessione
                HttpSession session = request.getSession();
                String titolo = (String) session.getAttribute("titolo");
                String autore = (String) session.getAttribute("autore");
                String categoria = (String) session.getAttribute("categoria");
                
                // Costruisco l'url con i parametri
                StringBuilder url = new StringBuilder("../jsp/admin/gestione-libri.jsp");
                if (titolo != null || autore != null || categoria != null) {
                    url.append("?");
                    if (titolo != null) url.append("titolo=").append(titolo);
                    if (autore != null) url.append("&autore=").append(autore);
                    if (categoria != null) url.append("&categoria=").append(categoria);
                }
                response.sendRedirect(url.toString());
            }

        } else if ("elimina".equals(azione)) {
            int id = Integer.parseInt(request.getParameter("id"));
            libroDAO.eliminaLibro(id);
            
            // Recupero i parametri di ricerca dalla sessione
            HttpSession session = request.getSession();
            String titolo = (String) session.getAttribute("titolo");
            String autore = (String) session.getAttribute("autore");
            String categoria = (String) session.getAttribute("categoria");
            
            // Costruisco l'rl con i parametri
            StringBuilder url = new StringBuilder("../jsp/admin/gestione-libri.jsp");
            if (titolo != null || autore != null || categoria != null) {
                url.append("?");
                if (titolo != null) url.append("titolo=").append(titolo);
                if (autore != null) url.append("&autore=").append(autore);
                if (categoria != null) url.append("&categoria=").append(categoria);
            }
            response.sendRedirect(url.toString());
        }
    }

    private void setLibroPropertiesFromRequest(Libro libro, HttpServletRequest request) {
        libro.setTitolo(request.getParameter("titolo"));
        libro.setAutore(request.getParameter("autore"));
        libro.setPrezzo(new BigDecimal(request.getParameter("prezzo")));
        libro.setDisponibilita(Integer.parseInt(request.getParameter("disponibilita")));
    }
}
