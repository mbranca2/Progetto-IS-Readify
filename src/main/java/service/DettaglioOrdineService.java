package service;

import model.DettaglioOrdine;

import java.util.List;

public interface DettaglioOrdineService extends BaseService<DettaglioOrdine, Integer> {
    List<DettaglioOrdine> findByOrdineId(int ordineId);
    boolean salvaTutti(List<DettaglioOrdine> dettagli);
}