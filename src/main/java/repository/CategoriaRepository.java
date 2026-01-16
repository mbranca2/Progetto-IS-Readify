package repository;

import model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends BaseRepository<Categoria, Integer> {
    Optional<Categoria> findByNome(String nome);
    List<Categoria> findByLibroId(int idLibro);
}