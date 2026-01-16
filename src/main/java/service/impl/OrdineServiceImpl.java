package service.impl;

import config.DependencyInjection;
import dto.DettaglioOrdineDTO;
import dto.OrdineDTO;
import model.DettaglioOrdine;
import model.Ordine;
import repository.OrdineRepository;
import repository.impl.OrdineRepositoryImpl;
import service.DettaglioOrdineService;
import service.OrdineService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrdineServiceImpl extends BaseServiceImpl<Ordine, Integer> implements OrdineService {

    private final OrdineRepository ordineRepository;
    private final DettaglioOrdineService dettaglioOrdineService;

    public OrdineServiceImpl() {
        super(DependencyInjection.getOrdineRepository());
        this.ordineRepository = (OrdineRepository) super.repository;
        this.dettaglioOrdineService = DependencyInjection.getDettaglioOrdineService();
    }

    @Override
    public List<Ordine> findByUtenteId(int utenteId) {
        return ordineRepository.findByUtenteId(utenteId);
    }

    @Override
    public boolean aggiornaStato(int idOrdine, String nuovoStato) {
        return ordineRepository.updateStato(idOrdine, nuovoStato);
    }

    @Override
    public OrdineDTO getOrdineConDettagli(int idOrdine) {
        return ordineRepository.findById(idOrdine)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<OrdineDTO> getOrdiniConDettagliByUtente(int utenteId) {
        return ordineRepository.findByUtenteId(utenteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrdineDTO convertToDTO(Ordine ordine) {
        if (ordine == null) {
            return null;
        }

        OrdineDTO dto = new OrdineDTO();
        dto.setIdOrdine(ordine.getIdOrdine());
        dto.setIdUtente(ordine.getIdUtente());
        dto.setIdIndirizzo(ordine.getIdIndirizzo());
        dto.setDataOrdine(ordine.getDataOrdine());
        dto.setStato(ordine.getStato());
        dto.setTotale(ordine.getTotale());

        // Converti i dettagli in DTO
        List<DettaglioOrdine> dettagli = dettaglioOrdineService.findByOrdineId(ordine.getIdOrdine());
        List<DettaglioOrdineDTO> dettagliDTO = new ArrayList<>();

        for (DettaglioOrdine dettaglio : dettagli) {
            DettaglioOrdineDTO dettaglioDTO = new DettaglioOrdineDTO();
            dettaglioDTO.setId(dettaglio.getId());
            dettaglioDTO.setIdOrdine(dettaglio.getIdOrdine());
            dettaglioDTO.setIdLibro(dettaglio.getIdLibro());
            dettaglioDTO.setQuantita(dettaglio.getQuantita());
            dettaglioDTO.setPrezzoUnitario(dettaglio.getPrezzoUnitario());
            dettaglioDTO.setTitoloLibro(dettaglio.getTitoloLibro());
            dettaglioDTO.setAutoreLibro(dettaglio.getAutoreLibro());
            dettaglioDTO.setIsbnLibro(dettaglio.getIsbnLibro());
            dettaglioDTO.setImmagineCopertina(dettaglio.getImmagineCopertina());

            dettagliDTO.add(dettaglioDTO);
        }

        dto.setDettagli(dettagliDTO);
        return dto;
    }
}