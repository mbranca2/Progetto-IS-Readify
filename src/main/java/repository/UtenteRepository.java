package repository;

import model.Utente;
import java.util.Optional;

public interface UtenteRepository extends BaseRepository<Utente, Integer> {
    Optional<Utente> findByEmail(String email);
    boolean existsByEmail(String email);
}