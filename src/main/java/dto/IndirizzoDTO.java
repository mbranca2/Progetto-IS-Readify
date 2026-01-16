package dto;

public class IndirizzoDTO {
    private int idIndirizzo;
    private int idUtente;
    private String via;
    private String citta;
    private String provincia;
    private String cap;
    private String paese;
    private String nomeDestinatario;
    private String cognomeDestinatario;
    private boolean preferito;

    // Getters and Setters
    public int getIdIndirizzo() { return idIndirizzo; }
    public void setIdIndirizzo(int idIndirizzo) { this.idIndirizzo = idIndirizzo; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

    public String getVia() { return via; }
    public void setVia(String via) { this.via = via; }

    public String getCitta() { return citta; }
    public void setCitta(String citta) { this.citta = citta; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getCap() { return cap; }
    public void setCap(String cap) { this.cap = cap; }

    public String getPaese() { return paese; }
    public void setPaese(String paese) { this.paese = paese; }

    public String getNomeDestinatario() { return nomeDestinatario; }
    public void setNomeDestinatario(String nomeDestinatario) { this.nomeDestinatario = nomeDestinatario; }

    public String getCognomeDestinatario() { return cognomeDestinatario; }
    public void setCognomeDestinatario(String cognomeDestinatario) { this.cognomeDestinatario = cognomeDestinatario; }

    public boolean isPreferito() { return preferito; }
    public void setPreferito(boolean preferito) { this.preferito = preferito; }
}