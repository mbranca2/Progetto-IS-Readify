package service.impl;

import config.DependencyInjection;
import dto.UtenteDTO;
import model.Utente;
import repository.UtenteRepository;
import repository.impl.UtenteRepositoryImpl;
import service.UtenteService;

import java.util.Optional;

public class UtenteServiceImpl extends BaseServiceImpl<Utente, Integer> implements UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteServiceImpl() {
        super(DependencyInjection.getUtenteRepository());
        this.utenteRepository = (UtenteRepository) super.repository;
    }

    @Override
    public Optional<Utente> findByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return utenteRepository.existsByEmail(email);
    }

    @Override
    public Utente register(UtenteDTO utenteDTO) {
        Utente utente = new Utente();
        utente.setEmail(utenteDTO.getEmail());
        utente.setPassword(utenteDTO.getPassword()); // In production, hash the password
        utente.setNome(utenteDTO.getNome());
        utente.setCognome(utenteDTO.getCognome());
        utente.setAdmin(false); // Default to non-admin

        return utenteRepository.save(utente);
    }

    @Override
    public Optional<Utente> login(String email, String password) {
        return utenteRepository.findByEmail(email)
                .filter(utente -> utente.getPassword().equals(password)); // In production, use password hashing
    }
}