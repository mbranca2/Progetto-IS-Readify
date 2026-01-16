package repository;

import model.Libro;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends BaseRepository<Libro, Integer> {
    List<Libro> findByTitolo(String titolo);
    List<Libro> findByAutore(String autore);
    List<Libro> findByCategoria(int categoriaId);
    boolean updateDisponibilita(int libroId, int quantita);
}
