package service.impl;

import config.DependencyInjection;
import dto.LibroDTO;
import model.Libro;
import repository.LibroRepository;
import repository.impl.LibroRepositoryImpl;
import service.LibroService;

import java.util.List;

public class LibroServiceImpl extends BaseServiceImpl<Libro, Integer> implements LibroService {

    private final LibroRepository libroRepository;

    public LibroServiceImpl() {
        super(DependencyInjection.getLibroRepository());
        this.libroRepository = (LibroRepository) super.repository;
    }

    @Override
    public List<Libro> findByTitolo(String titolo) {
        return libroRepository.findByTitolo(titolo);
    }

    @Override
    public List<Libro> findByAutore(String autore) {
        return libroRepository.findByAutore(autore);
    }

    @Override
    public List<Libro> findByCategoria(int categoriaId) {
        return libroRepository.findByCategoria(categoriaId);
    }

    @Override
    public boolean aggiornaDisponibilita(int libroId, int quantita) {
        return libroRepository.updateDisponibilita(libroId, -quantita); // Negative to reduce availability
    }

    @Override
    public Libro convertToEntity(LibroDTO libroDTO) {
        Libro libro = new Libro();
        libro.setId(libroDTO.getId());
        libro.setTitolo(libroDTO.getTitolo());
        libro.setAutore(libroDTO.getAutore());
        libro.setPrezzo(libroDTO.getPrezzo());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setDescrizione(libroDTO.getDescrizione());
        libro.setDisponibilita(libroDTO.getDisponibilita());
        libro.setCopertina(libroDTO.getCopertina());
        // libro.setCategoriaId(libroDTO.getCategoriaId());
        return libro;
    }

    @Override
    public LibroDTO convertToDTO(Libro libro) {
        LibroDTO dto = new LibroDTO();
        dto.setId(libro.getId());
        dto.setTitolo(libro.getTitolo());
        dto.setAutore(libro.getAutore());
        dto.setPrezzo(libro.getPrezzo());
        dto.setIsbn(libro.getIsbn());
        dto.setDescrizione(libro.getDescrizione());
        dto.setDisponibilita(libro.getDisponibilita());
        dto.setCopertina(libro.getCopertina());
        // dto.setCategoriaId(libro.getCategoriaId());
        return dto;
    }
}