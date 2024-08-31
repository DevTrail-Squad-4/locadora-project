package br.edu.solutis.dev.trail.locadora.service;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;

import java.util.List;

public abstract class CrudService<T> {

    public abstract T obterPorId( Long id) ;

    public abstract List<T> obterTodos();

    public abstract T salvar( T payload);

    public abstract void excluirPorId( Long id);
}
