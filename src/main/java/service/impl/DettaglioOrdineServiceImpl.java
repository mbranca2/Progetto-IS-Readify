package service.impl;

import config.DependencyInjection;
import model.DettaglioOrdine;
import repository.DettaglioOrdineRepository;
import repository.impl.DettaglioOrdineRepositoryImpl;
import service.DettaglioOrdineService;

import java.util.List;

public class DettaglioOrdineServiceImpl extends BaseServiceImpl<DettaglioOrdine, Integer> implements DettaglioOrdineService {

    private final DettaglioOrdineRepository dettaglioOrdineRepository;

    public DettaglioOrdineServiceImpl() {
        super(DependencyInjection.getDettaglioOrdineRepository());
        this.dettaglioOrdineRepository = (DettaglioOrdineRepository) super.repository;
    }

    @Override
    public List<DettaglioOrdine> findByOrdineId(int ordineId) {
        return dettaglioOrdineRepository.findByOrdineId(ordineId);
    }

    @Override
    public boolean salvaTutti(List<DettaglioOrdine> dettagli) {
        return dettaglioOrdineRepository.saveAll(dettagli);
    }
}