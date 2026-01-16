package service.impl;

import repository.BaseRepository;
import service.BaseService;

import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
    
    protected final BaseRepository<T, ID> repository;
    
    protected BaseServiceImpl(BaseRepository<T, ID> repository) {
        this.repository = repository;
    }
    
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
    
    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }
    
    @Override
    public T save(T entity) {
        return repository.save(entity);
    }
    
    @Override
    public T update(T entity) {
        return repository.save(entity);
    }
    
    @Override
    public void delete(ID id) {
        repository.delete(id);
    }
}
