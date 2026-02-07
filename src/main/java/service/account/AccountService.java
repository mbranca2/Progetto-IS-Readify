package service.account;

import model.bean.Indirizzo;
import model.bean.Utente;

import java.util.List;

public interface AccountService {

    Utente login(String email, String password);

    boolean register(RegistrationData data) throws AccountServiceException;

    boolean register(Utente utente);

    List<Indirizzo> listAddresses(int idUtente);

    boolean addAddress(int idUtente, Indirizzo indirizzo);

    boolean updateAddress(int idUtente, Indirizzo indirizzo);

    boolean deleteAddress(int idUtente, int idIndirizzo);

    boolean changePassword(int idUtente, String oldPassword, String newPassword);

    boolean updateProfile(Utente utente);
}
