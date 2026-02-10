package presentation.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidaPagamentoTest {

    @Test
    @DisplayName("TC2.2.5 Pagamento: dati validi -> nessun errore")
    void pagamentoValido_ok() {
        Map<String, String> err = ValidatorePagamento.validaPagamento(
                "Roberto Rossi",
                "5767991234001156",
                "12/30",
                "344"
        );
        assertTrue(err.isEmpty(), "Attesi 0 errori, trovati: " + err);
    }

    @Test
    @DisplayName("TC2.2.1 Pagamento: numero carta non valido -> errore numeroCarta")
    void numeroCartaErrato_fail() {
        Map<String, String> err = ValidatorePagamento.validaPagamento(
                "Roberto Rossi",
                "5767991234001156669",
                "12/30",
                "344"
        );
        assertTrue(err.containsKey("numeroCarta"));
    }

    @Test
    @DisplayName("TC2.2.2 Pagamento: scadenza non valida -> errore scadenza")
    void scadenzaMeseFuoriRange_fail() {
        Map<String, String> err = ValidatorePagamento.validaPagamento(
                "Roberto Rossi",
                "5767991234001156",
                "15/30",
                "344"
        );
        assertTrue(err.containsKey("scadenza"));
    }

    @Test
    @DisplayName("TC2.2.3 Pagamento: scadenza passata -> errore scadenza")
    void scadenzaPassata_fail() {
        Map<String, String> err = ValidatorePagamento.validaPagamento(
                "Roberto Rossi",
                "5767991234001156",
                "12/19",
                "344"
        );
        assertTrue(err.containsKey("scadenza"));
    }

    @Test
    @DisplayName("TC2.2.4 Pagamento: CVV non valido -> errore cvv")
    void cvvErrato_fail() {
        Map<String, String> err = ValidatorePagamento.validaPagamento(
                "Roberto Rossi",
                "5767991234001156",
                "12/30",
                "3445"
        );
        assertTrue(err.containsKey("cvv"));
    }
}