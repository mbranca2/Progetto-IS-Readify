package service;

import dto.LibroDTO;
import model.Libro;

import java.util.List;

public interface LibroService extends BaseService<Libro, Integer> {
    List<Libro> findByTitolo(String titolo);
    List<Libro> findByAutore(String autore);
    List<Libro> findByCategoria(int categoriaId);
    boolean aggiornaDisponibilita(int libroId, int quantita);
    Libro convertToEntity(LibroDTO libroDTO);
    LibroDTO convertToDTO(Libro libro);
}
