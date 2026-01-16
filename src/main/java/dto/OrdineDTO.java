package dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrdineDTO {
    private int idOrdine;
    private int idUtente;
    private int idIndirizzo;
    private Date dataOrdine;
    private String stato;
    private BigDecimal totale;
    private List<DettaglioOrdineDTO> dettagli;

    // Getters and Setters
    public int getIdOrdine() { return idOrdine; }
    public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public int getIdIndirizzo() { return idIndirizzo; }
    public void setIdIndirizzo(int idIndirizzo) { this.idIndirizzo = idIndirizzo; }

    public Date getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(Date dataOrdine) { this.dataOrdine = dataOrdine; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public BigDecimal getTotale() { return totale; }
    public void setTotale(BigDecimal totale) { this.totale = totale; }

    public List<DettaglioOrdineDTO> getDettagli() { return dettagli; }
    public void setDettagli(List<DettaglioOrdineDTO> dettagli) { this.dettagli = dettagli; }
}