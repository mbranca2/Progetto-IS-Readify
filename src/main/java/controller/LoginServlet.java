package controller;

import config.DependencyInjection;
import service.UtenteService;
import service.impl.UtenteServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UtenteService utenteService;

    public LoginServlet() {
        this.utenteService = DependencyInjection.getUtenteService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        utenteService.login(email, password).ifPresentOrElse(
                utente -> {
                    // Login successful
                    HttpSession session = request.getSession();
                    session.setAttribute("utente", utente);
                    try {
                        response.sendRedirect(request.getContextPath() + "/listalibri");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },
                () -> {
                    // Login failed
                    request.setAttribute("error", "Email o password non validi");
                    try {
                        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                    } catch (ServletException | IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}