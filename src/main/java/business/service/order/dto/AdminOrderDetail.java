package business.service.order.dto;

import business.model.Indirizzo;
import business.model.Ordine;
import business.model.Utente;

public class AdminOrderDetail {

    private final Ordine ordine;
    private final Utente utente;
    private final Indirizzo indirizzo;

    public AdminOrderDetail(Ordine ordine, Utente utente, Indirizzo indirizzo) {
        this.ordine = ordine;
        this.utente = utente;
        this.indirizzo = indirizzo;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public Utente getUtente() {
        return utente;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }
}
