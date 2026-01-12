package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.LibroDAO;

import java.io.IOException;

@WebServlet("/admin/libri/elimina")
public class EliminaLibroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final LibroDAO libroDAO = new LibroDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Prendo l'ID del libro da eliminare
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID libro mancante");
            return;
        }

        try {
            int idLibro = Integer.parseInt(idParam);

            // Provo a eliminare il libro
            boolean eliminato = libroDAO.eliminaLibro(idLibro);

            if (eliminato) {
                // Reindirizza alla lista libri con messaggio di successo
                response.sendRedirect(request.getContextPath() +
                        "/admin/libri?success=Libro eliminato con successo");
            } else {
                // Se il libro non esiste o non può essere eliminato
                response.sendRedirect(request.getContextPath() +
                        "/admin/libri?errore=Impossibile eliminare il libro. Potrebbe essere già stato rimosso o non esistere.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID libro non valido");
        } catch (Exception e) {
            // Log dell'errore
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/admin/libri?errore=Si è verificato un errore durante l'eliminazione del libro");
        }
    }
}
