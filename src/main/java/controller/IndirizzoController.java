package controller;

import dto.IndirizzoDTO;
import service.IndirizzoService;
import config.DependencyInjection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/indirizzi/*")
public class IndirizzoController extends HttpServlet {
    private final IndirizzoService indirizzoService = DependencyInjection.getIndirizzoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        int userId = getCurrentUserId(request); // Implement this method

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all addresses for the current user
            List<IndirizzoDTO> indirizzi = indirizzoService.findDTOByUtenteId(userId);
            request.setAttribute("indirizzi", indirizzi);
            request.getRequestDispatcher("/WEB-INF/views/indirizzi/lista.jsp").forward(request, response);
        } else if (pathInfo.matches("/\\d+")) {
            // Get single address
            int id = Integer.parseInt(pathInfo.substring(1));
            indirizzoService.findDTOById(id).ifPresentOrElse(
                    indirizzo -> {
                        request.setAttribute("indirizzo", indirizzo);
                        try {
                            request.getRequestDispatcher("/WEB-INF/views/indirizzi/dettaglio.jsp")
                                    .forward(request, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    },
                    () -> response.setStatus(HttpServletResponse.SC_NOT_FOUND)
            );
        } else if (pathInfo.equals("/nuovo")) {
            // Show form for new address
            request.getRequestDispatcher("/WEB-INF/views/indirizzi/nuovo.jsp").forward(request, response);
        } else if (pathInfo.equals("/preferito")) {
            // Show preferred address
            indirizzoService.findPreferitoByUtenteId(userId).ifPresentOrElse(
                    indirizzo -> {
                        request.setAttribute("indirizzo", indirizzo);
                        try {
                            request.getRequestDispatcher("/WEB-INF/views/indirizzi/dettaglio.jsp")
                                    .forward(request, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    },
                    () -> response.setStatus(HttpServletResponse.SC_NOT_FOUND)
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle form submission for new/update address
        String pathInfo = request.getPathInfo();
        int userId = getCurrentUserId(request);

        if (pathInfo == null || pathInfo.equals("/")) {
            // Create new address
            // Implementation depends on your form structure
        } else if (pathInfo.matches("/\\d+/preferito")) {
            // Set as preferred address
            int idIndirizzo = Integer.parseInt(pathInfo.substring(1, pathInfo.lastIndexOf('/')));
            if (indirizzoService.setIndirizzoPreferito(idIndirizzo, userId)) {
                response.sendRedirect(request.getContextPath() + "/indirizzi");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    private int getCurrentUserId(HttpServletRequest request) {
        // Implement logic to get the current user's ID from the session
        // This is just a placeholder
        return 1; // Replace with actual user ID from session
    }
}