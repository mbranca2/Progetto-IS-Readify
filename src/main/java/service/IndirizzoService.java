package service;

import dto.IndirizzoDTO;
import model.Indirizzo;

import java.util.List;
import java.util.Optional;

public interface IndirizzoService extends BaseService<Indirizzo, Integer> {
    List<Indirizzo> findByUtenteId(int idUtente);
    Optional<Indirizzo> findPreferitoByUtenteId(int idUtente);
    boolean setIndirizzoPreferito(int idIndirizzo, int idUtente);
    List<IndirizzoDTO> findDTOByUtenteId(int idUtente);
    Optional<IndirizzoDTO> findDTOById(int id);
}