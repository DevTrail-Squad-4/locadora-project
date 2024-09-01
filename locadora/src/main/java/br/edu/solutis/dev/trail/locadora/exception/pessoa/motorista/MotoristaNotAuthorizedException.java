package br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista;

public class MotoristaNotAuthorizedException extends RuntimeException {
    public MotoristaNotAuthorizedException(Long id) {
        super("O motorista com o ID " + id + " não está autorizado a dirigir esse veículo.");
    }
}

