package dto;

import java.math.BigDecimal;

public class DettaglioOrdineDTO {
    private int id;
    private int idOrdine;
    private int idLibro;
    private int quantita;
    private BigDecimal prezzoUnitario;
    private String titoloLibro;
    private String autoreLibro;
    private String isbnLibro;
    private String immagineCopertina;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdOrdine() { return idOrdine; }
    public void setIdOrdine(int idOrdine) { this.idOrdine = idOrdine; }
    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }
    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public BigDecimal getPrezzoUnitario() { return prezzoUnitario; }
    public void setPrezzoUnitario(BigDecimal prezzoUnitario) { this.prezzoUnitario = prezzoUnitario; }
    public String getTitoloLibro() { return titoloLibro; }
    public void setTitoloLibro(String titoloLibro) { this.titoloLibro = titoloLibro; }
    public String getAutoreLibro() { return autoreLibro; }
    public void setAutoreLibro(String autoreLibro) { this.autoreLibro = autoreLibro; }
    public String getIsbnLibro() { return isbnLibro; }
    public void setIsbnLibro(String isbnLibro) { this.isbnLibro = isbnLibro; }
    public String getImmagineCopertina() { return immagineCopertina; }
    public void setImmagineCopertina(String immagineCopertina) { this.immagineCopertina = immagineCopertina; }
}