package repository;

import model.Ordine;
import java.util.List;

public interface OrdineRepository extends BaseRepository<Ordine, Integer> {
    List<Ordine> findByUtenteId(int utenteId);
    boolean updateStato(int idOrdine, String nuovoStato);
}