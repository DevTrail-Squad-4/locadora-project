package br.edu.solutis.dev.trail.locadora.service;

import br.edu.solutis.dev.trail.locadora.response.PageResponse;

public interface CrudService<T> {
    T findById(Long id);

    PageResponse<T> findAll(int pageNo, int pageSize);

    T add(T payload);

    T update(T payload);

    void deleteById(Long id);
}