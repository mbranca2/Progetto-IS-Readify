package service.review.impl;

import model.bean.Recensione;
import model.dao.OrdineDAO;
import model.dao.RecensioneDAO;
import service.review.ReviewService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ReviewServiceImpl implements ReviewService {

    private final RecensioneDAO recensioneDAO;
    private final OrdineDAO ordineDAO;

    public ReviewServiceImpl(RecensioneDAO recensioneDAO, OrdineDAO ordineDAO) {
        this.recensioneDAO = Objects.requireNonNull(recensioneDAO);
        this.ordineDAO = Objects.requireNonNull(ordineDAO);
    }

    @Override
    public List<Recensione> listByBook(int idLibro) {
        if (idLibro <= 0) return Collections.emptyList();
        try {
            return recensioneDAO.trovaRecensioniPerLibro(idLibro);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean canUserReview(int idUtente, int idLibro) {
        if (idUtente <= 0 || idLibro <= 0) return false;
        return ordineDAO.hasUserPurchasedBook(idUtente, idLibro);
    }

    @Override
    public boolean addReview(Recensione recensione) {
        if (recensione == null) return false;

        int idLibro = recensione.getIdLibro();
        int idUtente = recensione.getIdUtente();

        if (idLibro <= 0) return false;
        if (idUtente <= 0) return false;

        int voto = recensione.getVoto();
        if (voto < 1 || voto > 5) return false;

        // RF_REC_1: recensione consentita solo se il libro è stato acquistato dall'utente
        if (!canUserReview(idUtente, idLibro)) {
            return false;
        }

        String commento = recensione.getCommento();
        if (commento == null) commento = "";
        commento = commento.trim();

        if (commento.length() > 2000) {
            commento = commento.substring(0, 2000);
        }
        recensione.setCommento(commento);

        try {
            return recensioneDAO.inserisciRecensione(recensione);
        } catch (Exception e) {
            return false;
        }
    }
}
