package repository;

import model.DettaglioOrdine;
import java.util.List;

public interface DettaglioOrdineRepository extends BaseRepository<DettaglioOrdine, Integer> {
    List<DettaglioOrdine> findByOrdineId(int ordineId);
    boolean saveAll(List<DettaglioOrdine> dettagli);
}