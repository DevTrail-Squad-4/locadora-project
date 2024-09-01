package br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista;

public class MotoristaNotFoundException extends RuntimeException {
    public MotoristaNotFoundException(Long id) {
        super("Motorista com ID " + id + " não foi achado.");
    }
}
