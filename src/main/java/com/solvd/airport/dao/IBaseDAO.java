package com.solvd.airport.dao;

import java.util.List;

public interface IBaseDAO<T> {
    T save (T entity);
    void update(T entity);
    T getById(Long id);
    void deleteById(Long id);
    List<T> getAll();
}