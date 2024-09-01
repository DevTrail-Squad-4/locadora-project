package br.edu.solutis.dev.trail.locadora.exception.pessoa.funcionario;

public class FuncionarioNotFoundException extends RuntimeException {
    public FuncionarioNotFoundException(Long id) {
        super("Employee with ID " + id + " not found.");
    }
}
