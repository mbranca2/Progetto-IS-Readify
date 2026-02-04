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
                "/WEB-INF/jsp/gestioneAccount.jsp",
                "/WEB-INF/jsp/ordini.jsp",
                "/WEB-INF/jsp/admin/*"
        }
)

public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/WEB-INF/jsp/login.jsp";

        boolean loggedIn = session != null && session.getAttribute("utente") != null;
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);
        if (loggedIn || loginRequest) {
            chain.doFilter(request, response);
        } else {
            String requestedURL = httpRequest.getRequestURL().toString();
            String queryString = httpRequest.getQueryString();
            if (queryString != null && !queryString.isEmpty()) {
                requestedURL += "?" + queryString;
            }
            session = httpRequest.getSession(true);
            session.setAttribute("redirectAfterLogin", requestedURL);

            if (requestedURL != null && requestedURL.contains("/WEB-INF/jsp/admin/")) {
                if (session != null && session.getAttribute("utente") != null) {
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/?error=unauthorized");
                    return;
                }
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/WEB-INF/jsp/login.jsp?redirect=true&admin=true");
            } else {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/WEB-INF/jsp/login.jsp?redirect=true");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
