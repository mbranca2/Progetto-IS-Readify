package service;

import dto.OrdineDTO;
import model.Ordine;

import java.util.List;

public interface OrdineService extends BaseService<Ordine, Integer> {
    List<Ordine> findByUtenteId(int utenteId);
    boolean aggiornaStato(int idOrdine, String nuovoStato);
    OrdineDTO getOrdineConDettagli(int idOrdine);
    List<OrdineDTO> getOrdiniConDettagliByUtente(int utenteId);
}