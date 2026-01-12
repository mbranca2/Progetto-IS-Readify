package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidatoreForm {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Requisiti minimi password:
    // - 8+ caratteri
    // - Una maiuscola
    // - Una minuscola
    // - Un numero
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^\\+?[0-9\\s-]{6,}$");

    public static Map<String, String> validaRegistrazione(
            String nome, String cognome, String email, 
            String password, String confermaPassword, 
            String telefono, boolean privacyAccettata) {
        
        Map<String, String> errori = new HashMap<>();
        
        // Validazione nome
        if (nome == null || nome.trim().isEmpty()) {
            errori.put("nome", "Il nome è obbligatorio");
        } else if (nome.trim().length() < 2) {
            errori.put("nome", "Il nome deve contenere almeno 2 caratteri");
        } else if (nome.length() > 50) {
            errori.put("nome", "Il nome non può superare i 50 caratteri");
        }
        
        // Validazione cognome
        if (cognome == null || cognome.trim().isEmpty()) {
            errori.put("cognome", "Il cognome è obbligatorio");
        } else if (cognome.trim().length() < 2) {
            errori.put("cognome", "Il cognome deve contenere almeno 2 caratteri");
        } else if (cognome.length() > 50) {
            errori.put("cognome", "Il cognome non può superare i 50 caratteri");
        }
        
        // Validazione email
        if (email == null || email.trim().isEmpty()) {
            errori.put("email", "L'email è obbligatoria");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errori.put("email", "Inserisci un indirizzo email valido");
        } else if (email.length() > 100) {
            errori.put("email", "L'email non può superare i 100 caratteri");
        }
        
        // Validazione password
        if (password == null || password.isEmpty()) {
            errori.put("password", "La password è obbligatoria");
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errori.put("password", 
                    "La password deve contenere almeno 8 caratteri, " +
                    "una lettera maiuscola, una minuscola e un numero");
        }
        
        // Validazione conferma password
        if (!password.equals(confermaPassword)) {
            errori.put("confermaPassword", "Le password non coincidono");
        }
        
        // Validazione telefono (opzionale)
        if (telefono != null && !telefono.trim().isEmpty() && 
                !TELEFONO_PATTERN.matcher(telefono).matches()) {
            errori.put("telefono", "Inserisci un numero di telefono valido");
        }
        
        // Validazione accettazione privacy
        if (!privacyAccettata) {
            errori.put("privacy", "È necessario accettare l'informativa sulla privacy");
        }
        
        return errori;
    }

    public static String pulisciInput(String input) {
        if (input == null) {
            return null;
        }
        // Rimuove spazi bianchi all'inizio e alla fine
        String pulito = input.trim();
        pulito = pulito.replace("<", "&lt;")
                      .replace(">", "&gt;")
                      .replace("\"", "&quot;")
                      .replace("'", "&#x27;")
                      .replace("/", "&#x2F;");
        return pulito;
    }
}
