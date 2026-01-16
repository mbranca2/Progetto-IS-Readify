package service.impl;

import config.DependencyInjection;
import dto.CategoriaDTO;
import model.Categoria;
import repository.CategoriaRepository;
import repository.impl.CategoriaRepositoryImpl;
import service.CategoriaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoriaServiceImpl extends BaseServiceImpl<Categoria, Integer> implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaServiceImpl() {
        super(DependencyInjection.getCategoriaRepository());
        this.categoriaRepository = (CategoriaRepository) super.repository;
    }

    @Override
    public Optional<Categoria> findByNome(String nome) {
        return categoriaRepository.findByNome(nome);
    }

    @Override
    public List<Categoria> findByLibroId(int idLibro) {
        return categoriaRepository.findByLibroId(idLibro);
    }

    @Override
    public List<CategoriaDTO> findAllDTO() {
        return categoriaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoriaDTO> findDTOById(int id) {
        return categoriaRepository.findById(id)
                .map(this::convertToDTO);
    }

    private CategoriaDTO convertToDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }

        CategoriaDTO dto = new CategoriaDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNome(categoria.getNome());
        dto.setDescrizione(categoria.getDescrizione());
        return dto;
    }
}