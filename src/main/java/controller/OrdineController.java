package controller;

import dto.OrdineDTO;
import service.OrdineService;
import config.DependencyInjection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ordini/*")
public class OrdineController extends HttpServlet {
    private final OrdineService ordineService = DependencyInjection.getOrdineService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all orders for the current user
            int userId = getCurrentUserId(request); // Implement this method
            List<OrdineDTO> ordini = ordineService.getOrdiniConDettagliByUtente(userId);
            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/WEB-INF/views/ordini/lista.jsp").forward(request, response);
        } else if (pathInfo.matches("/\\d+")) {
            // Get single order
            int orderId = Integer.parseInt(pathInfo.substring(1));
            OrdineDTO ordine = ordineService.getOrdineConDettagli(orderId);

            if (ordine != null) {
                request.setAttribute("ordine", ordine);
                request.getRequestDispatcher("/WEB-INF/views/ordini/dettaglio.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private int getCurrentUserId(HttpServletRequest request) {
        // Implement logic to get the current user's ID from the session
        // This is just a placeholder
        return 1; // Replace with actual user ID from session
    }
}