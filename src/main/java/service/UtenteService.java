package service;

import dto.UtenteDTO;
import model.Utente;

import java.util.Optional;

public interface UtenteService extends BaseService<Utente, Integer> {
    Optional<Utente> findByEmail(String email);
    boolean existsByEmail(String email);
    Utente register(UtenteDTO utenteDTO);
    Optional<Utente> login(String email, String password);
}