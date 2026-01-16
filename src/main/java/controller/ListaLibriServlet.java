package controller;

import model.Libro;
import service.LibroService;
import service.impl.LibroServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/listalibri")
public class ListaLibriServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final LibroService libroService;

    public ListaLibriServlet() {
        this.libroService = new LibroServiceImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchTerm = request.getParameter("search");
        String categoriaId = request.getParameter("categoria");

        List<Libro> libri;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            // Search by title or author
            libri = libroService.findByTitolo(searchTerm);
            if (libri.isEmpty()) {
                libri = libroService.findByAutore(searchTerm);
            }
        } else if (categoriaId != null && !categoriaId.trim().isEmpty()) {
            // Filter by category
            try {
                int id = Integer.parseInt(categoriaId);
                libri = libroService.findByCategoria(id);
            } catch (NumberFormatException e) {
                libri = libroService.findAll();
            }
        } else {
            // Get all books
            libri = libroService.findAll();
        }

        request.setAttribute("libri", libri);
        request.getRequestDispatcher("/jsp/catalogo.jsp").forward(request, response);
    }
}