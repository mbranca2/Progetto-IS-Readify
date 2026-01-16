package controller;

import dto.CategoriaDTO;
import service.CategoriaService;
import config.DependencyInjection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/categorie/*")
public class CategoriaController extends HttpServlet {
    private final CategoriaService categoriaService = DependencyInjection.getCategoriaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all categories
            List<CategoriaDTO> categorie = categoriaService.findAllDTO();
            request.setAttribute("categorie", categorie);
            request.getRequestDispatcher("/WEB-INF/views/categorie/lista.jsp").forward(request, response);
        } else if (pathInfo.matches("/\\d+")) {
            // Get single category
            int id = Integer.parseInt(pathInfo.substring(1));
            Optional<CategoriaDTO> categoria = categoriaService.findDTOById(id);

            if (categoria.isPresent()) {
                request.setAttribute("categoria", categoria.get());
                request.getRequestDispatcher("/WEB-INF/views/categorie/dettaglio.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else if (pathInfo.equals("/nuovo")) {
            // Show form for new category
            request.getRequestDispatcher("/WEB-INF/views/categorie/nuovo.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle form submission for new/update category
        // Implementation depends on your form structure
    }
}