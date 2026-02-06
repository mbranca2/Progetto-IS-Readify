package service.catalog;

import model.bean.Libro;

public interface AdminCatalogService {

    Libro getBookById(int idLibro);

    /**
     * Inserisce un nuovo libro nel catalogo.
     */
    boolean addBook(Libro newBook);

    /**
     * Aggiorna un libro e notifica gli observer (stock/price/book updated).
     */
    boolean updateBook(Libro updatedBook);

    /**
     * Rimuove un libro dal catalogo e notifica gli observer.
     */
    boolean removeBook(int idLibro);
}
