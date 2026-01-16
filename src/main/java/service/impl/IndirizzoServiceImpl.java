package service.impl;

import config.DependencyInjection;
import dto.IndirizzoDTO;
import model.Indirizzo;
import repository.IndirizzoRepository;
import repository.impl.IndirizzoRepositoryImpl;
import service.IndirizzoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IndirizzoServiceImpl extends BaseServiceImpl<Indirizzo, Integer> implements IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;

    public IndirizzoServiceImpl() {
        super(DependencyInjection.getIndirizzoRepository());
        this.indirizzoRepository = (IndirizzoRepository) super.repository;
    }

    @Override
    public List<Indirizzo> findByUtenteId(int idUtente) {
        return indirizzoRepository.findByUtenteId(idUtente);
    }

    @Override
    public Optional<Indirizzo> findPreferitoByUtenteId(int idUtente) {
        return indirizzoRepository.findPreferitoByUtenteId(idUtente);
    }

    @Override
    public boolean setIndirizzoPreferito(int idIndirizzo, int idUtente) {
        // First, unset any existing preferred address
        indirizzoRepository.findPreferitoByUtenteId(idUtente)
                .ifPresent(currentPref -> indirizzoRepository.updatePreferito(currentPref.getIdIndirizzo(), false));

        // Set the new preferred address
        return indirizzoRepository.updatePreferito(idIndirizzo, true);
    }

    @Override
    public List<IndirizzoDTO> findDTOByUtenteId(int idUtente) {
        return indirizzoRepository.findByUtenteId(idUtente).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IndirizzoDTO> findDTOById(int id) {
        return indirizzoRepository.findById(id)
                .map(this::convertToDTO);
    }

    private IndirizzoDTO convertToDTO(Indirizzo indirizzo) {
        if (indirizzo == null) {
            return null;
        }

        IndirizzoDTO dto = new IndirizzoDTO();
        dto.setIdIndirizzo(indirizzo.getIdIndirizzo());
        dto.setIdUtente(indirizzo.getIdUtente());
        dto.setVia(indirizzo.getVia());
        dto.setCitta(indirizzo.getCitta());
        dto.setProvincia(indirizzo.getProvincia());
        dto.setCap(indirizzo.getCap());
        dto.setPaese(indirizzo.getPaese());
        dto.setNomeDestinatario(indirizzo.getNomeDestinatario());
        dto.setCognomeDestinatario(indirizzo.getCognomeDestinatario());
        dto.setPreferito(indirizzo.isPreferito());

        return dto;
    }
}