package service.account.impl;

import model.bean.Utente;
import model.dao.UtenteDAO;
import service.account.AdminUserService;

import java.util.List;
import java.util.Objects;

public class AdminUserServiceImpl implements AdminUserService {

    private final UtenteDAO utenteDAO;

    public AdminUserServiceImpl(UtenteDAO utenteDAO) {
        this.utenteDAO = Objects.requireNonNull(utenteDAO);
    }

    @Override
    public List<Utente> listAll() {
        return utenteDAO.trovaTuttiUtenti();
    }

    @Override
    public Utente getById(int idUtente) {
        if (idUtente <= 0) return null;
        return utenteDAO.trovaUtentePerId(idUtente);
    }

    @Override
    public boolean create(Utente utente) {
        if (utente == null) return false;
        try {
            return utenteDAO.inserisciUtente(utente);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean update(Utente utente) {
        if (utente == null || utente.getIdUtente() <= 0) return false;
        try {
            return utenteDAO.aggiornaUtente(utente);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(int idUtente) {
        if (idUtente <= 0) return false;
        try {
            return utenteDAO.eliminaUtente(idUtente);
        } catch (Exception e) {
            return false;
        }
    }
}
