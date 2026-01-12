package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(
    filterName = "AuthenticationFilter",
    urlPatterns = {
        "/checkout",
        "/jsp/gestioneAccount.jsp",
        "/jsp/ordini.jsp",
        "/jsp/admin/*"
    }
)
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String loginURI = httpRequest.getContextPath() + "/jsp/login.jsp";
        
        boolean loggedIn = session != null && session.getAttribute("utente") != null;
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);
        
        if (loggedIn || loginRequest) {
            chain.doFilter(request, response);
        } else {
            // Memorizzo la pagina richiesta per tornare dopo ogin
            String requestedURL = httpRequest.getRequestURL().toString();
            String queryString = httpRequest.getQueryString();
            
            if (queryString != null && !queryString.isEmpty()) {
                requestedURL += "?" + queryString;
            }
            
            session = httpRequest.getSession(true);
            session.setAttribute("redirectAfterLogin", requestedURL);
            
            // Controllo se l'utente sta accedendo a admind ashboard
            if (requestedURL != null && requestedURL.contains("/jsp/admin/")) {
                // Utente loggato ma non admin: reindirizzo a home
                if (session != null && session.getAttribute("utente") != null) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/?error=unauthorized");
                    return;
                }
                //se non  loggato, lo mando alogin
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/login.jsp?redirect=true&admin=true");
            } else {
                // login standard per gli utenti normali
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/login.jsp?redirect=true");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
