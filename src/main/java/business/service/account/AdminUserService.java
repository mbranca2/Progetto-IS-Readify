package business.service.account;

import business.model.Utente;

import java.util.List;

public interface AdminUserService {

    List<Utente> listAll();

    Utente getById(int idUtente);

    boolean create(Utente utente);

    boolean create(Utente utente, String rawPassword);

    boolean update(Utente utente);

    boolean update(Utente utente, String rawPassword);

    boolean delete(int idUtente);
}
