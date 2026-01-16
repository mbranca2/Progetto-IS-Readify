package service;

import dto.CategoriaDTO;
import model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService extends BaseService<Categoria, Integer> {
    Optional<Categoria> findByNome(String nome);
    List<Categoria> findByLibroId(int idLibro);
    List<CategoriaDTO> findAllDTO();
    Optional<CategoriaDTO> findDTOById(int id);
}