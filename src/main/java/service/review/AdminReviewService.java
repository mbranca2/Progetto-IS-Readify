package service.review;

import service.review.dto.AdminReviewRow;

import java.util.List;

public interface AdminReviewService {

    List<AdminReviewRow> listAll();

    boolean deleteById(int idRecensione);
}
