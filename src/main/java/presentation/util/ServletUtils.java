package presentation.util;

import business.model.Utente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;

public final class ServletUtils {
    private ServletUtils() {
        throw new UnsupportedOperationException("Classe utility non istanziabile");
    }

    /**
     * Converte una stringa in int in modo sicuro.
     *
     * @param value Stringa da convertire
     * @return Il valore convertito o -1 se la conversione fallisce
     */
    public static int parseIntSafe(String value) {
        return parseIntSafe(value, -1);
    }

    /**
     * Converte una stringa in int in modo sicuro con valore di default personalizzato.
     *
     * @param value Stringa da convertire
     * @param defaultValue Valore di default se la conversione fallisce
     * @return Il valore convertito o defaultValue se la conversione fallisce
     */
    public static int parseIntSafe(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Converte una stringa in BigDecimal in modo sicuro.
     *
     * @param value Stringa da convertire
     * @return Il valore convertito o null se la conversione fallisce
     */
    public static BigDecimal parseBigDecimalSafe(String value) {
        return parseBigDecimalSafe(value, null);
    }

    /**
     * Converte una stringa in BigDecimal in modo sicuro con valore di default.
     *
     * @param value Stringa da convertire
     * @param defaultValue Valore di default se la conversione fallisce
     * @return Il valore convertito o defaultValue se la conversione fallisce
     */
    public static BigDecimal parseBigDecimalSafe(String value, BigDecimal defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Verifica se una richiesta è AJAX.
     *
     * @param request La HttpServletRequest
     * @return true se è una richiesta AJAX, false altrimenti
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    /**
     * Ottiene l'utente corrente dalla sessione.
     *
     * @param request La HttpServletRequest
     * @return L'utente corrente o null se non autenticato
     */
    public static Utente getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object utente = session.getAttribute("utente");
        return utente instanceof Utente ? (Utente) utente : null;
    }

    /**
     * Verifica se l'utente è autenticato.
     *
     * @param request La HttpServletRequest
     * @return true se l'utente è autenticato, false altrimenti
     */
    public static boolean isAuthenticated(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }

    /**
     * Verifica se l'utente corrente è admin.
     *
     * @param request La HttpServletRequest
     * @return true se l'utente è admin, false altrimenti
     */
    public static boolean isAdmin(HttpServletRequest request) {
        Utente utente = getCurrentUser(request);
        return utente != null && "admin".equalsIgnoreCase(utente.getRuolo());
    }

    /**
     * Ottiene un parametro String dalla request con trim e controllo null.
     *
     * @param request La HttpServletRequest
     * @param paramName Nome del parametro
     * @return Il valore del parametro trimmed o null se vuoto/assente
     */
    public static String getParameterTrimmed(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    /**
     * Ottiene un parametro String dalla request con valore di default.
     *
     * @param request La HttpServletRequest
     * @param paramName Nome del parametro
     * @param defaultValue Valore di default
     * @return Il valore del parametro o defaultValue se vuoto/assente
     */
    public static String getParameterOrDefault(HttpServletRequest request, String paramName, String defaultValue) {
        String value = getParameterTrimmed(request, paramName);
        return value != null ? value : defaultValue;
    }
}