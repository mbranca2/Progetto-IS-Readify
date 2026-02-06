package service.review;

import model.bean.Recensione;

import java.util.List;

public interface ReviewService {

    List<Recensione> listByBook(int idLibro);

    boolean addReview(Recensione recensione);

    boolean canUserReview(int idUtente, int idLibro);

    boolean updateReview(int idRecensione, int idUtente, int voto, String commento);

    boolean deleteReview(int idRecensione, int idUtente);
}
