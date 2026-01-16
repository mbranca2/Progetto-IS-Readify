package repository;

import model.Indirizzo;

import java.util.List;
import java.util.Optional;

public interface IndirizzoRepository extends BaseRepository<Indirizzo, Integer> {
    List<Indirizzo> findByUtenteId(int idUtente);
    Optional<Indirizzo> findPreferitoByUtenteId(int idUtente);
    boolean updatePreferito(int idIndirizzo, boolean preferito);
}