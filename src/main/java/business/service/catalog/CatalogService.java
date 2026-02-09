package business.service.catalog;

import business.model.Libro;

import java.math.BigDecimal;
import java.util.List;

public interface CatalogService {
    List<Libro> listAll();

    List<Libro> search(String titolo, String autore, String categoriaId, BigDecimal prezzoMin, BigDecimal prezzoMax, int page, int pageSize);

    int count(String titolo, String autore, String categoriaId, BigDecimal prezzoMin, BigDecimal prezzoMax);

    default List<Libro> search(String titolo, String autore, String categoriaId, int page, int pageSize) {
        return search(titolo, autore, categoriaId, null, null, page, pageSize);
    }

    default int count(String titolo, String autore, String categoriaId) {
        return count(titolo, autore, categoriaId, null, null);
    }

    Libro getById(int idLibro);
}
