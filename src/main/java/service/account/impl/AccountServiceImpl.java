package service.account.impl;

import model.bean.Indirizzo;
import model.bean.Utente;
import model.dao.IndirizzoDAO;
import model.dao.UtenteDAO;
import service.account.AccountService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AccountServiceImpl implements AccountService {

    private final UtenteDAO utenteDAO;
    private final IndirizzoDAO indirizzoDAO;

    public AccountServiceImpl(UtenteDAO utenteDAO, IndirizzoDAO indirizzoDAO) {
        this.utenteDAO = Objects.requireNonNull(utenteDAO);
        this.indirizzoDAO = Objects.requireNonNull(indirizzoDAO);
    }

    @Override
    public Utente login(String email, String password) {
        if (email == null || email.trim().isEmpty()) return null;
        if (password == null) return null;
        try {
            return UtenteDAO.login(email.trim(), password);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean register(Utente utente) {
        if (utente == null) return false;
        try {
            return utenteDAO.inserisciUtente(utente);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Indirizzo> listAddresses(int idUtente) {
        if (idUtente <= 0) return Collections.emptyList();
        try {
            return indirizzoDAO.trovaIndirizziPerUtente(idUtente);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean addAddress(int idUtente, Indirizzo indirizzo) {
        if (idUtente <= 0 || indirizzo == null) return false;
        indirizzo.setIdUtente(idUtente);
        try {
            return indirizzoDAO.inserisciIndirizzo(indirizzo);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateAddress(int idUtente, Indirizzo indirizzo) {
        if (idUtente <= 0 || indirizzo == null || indirizzo.getIdIndirizzo() <= 0) return false;
        Indirizzo existing = indirizzoDAO.trovaIndirizzoPerId(indirizzo.getIdIndirizzo());
        if (existing == null || existing.getIdUtente() != idUtente) return false;

        indirizzo.setIdUtente(idUtente);

        try {
            // aggiornaIndirizzo già contiene AND id_utente = ?
            return indirizzoDAO.aggiornaIndirizzo(indirizzo);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAddress(int idUtente, int idIndirizzo) {
        return false;
    }

    @Override
    public boolean changePassword(int idUtente, String oldPassword, String newPassword) {
        if (idUtente <= 0) return false;
        if (oldPassword == null || oldPassword.isEmpty()) return false;
        if (newPassword == null || newPassword.trim().length() < 6) return false;

        try {
            return utenteDAO.changePassword(idUtente, oldPassword, newPassword.trim());
        } catch (Exception e) {
            return false;
        }
    }
}
