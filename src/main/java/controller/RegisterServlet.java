package controller;

import config.DependencyInjection;
import dto.UtenteDTO;
import service.UtenteService;
import service.impl.UtenteServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UtenteService utenteService;

    public RegisterServlet() {
        this.utenteService = DependencyInjection.getUtenteService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UtenteDTO utenteDTO = new UtenteDTO();
        utenteDTO.setEmail(request.getParameter("email"));
        utenteDTO.setPassword(request.getParameter("password")); // In production, hash the password
        utenteDTO.setNome(request.getParameter("nome"));
        utenteDTO.setCognome(request.getParameter("cognome"));

        if (utenteService.existsByEmail(utenteDTO.getEmail())) {
            request.setAttribute("error", "Email già registrata");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        utenteService.register(utenteDTO);
        response.sendRedirect(request.getContextPath() + "/login?registered=true");
    }
}