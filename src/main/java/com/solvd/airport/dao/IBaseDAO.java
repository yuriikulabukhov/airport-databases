package com.solvd.airport.dao;

import java.util.List;
import java.util.Optional;

public interface IBaseDAO<T> {
    T save(T entity);
    void update(T entity);
    Optional<T> getById(Long id);
    void deleteById(Long id);
    List<T> getAll();
}