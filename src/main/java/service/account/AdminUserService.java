package service.account;

import model.bean.Utente;

import java.util.List;

public interface AdminUserService {

    List<Utente> listAll();

    Utente getById(int idUtente);

    boolean create(Utente utente);

    boolean update(Utente utente);

    boolean delete(int idUtente);
}
