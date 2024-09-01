package br.edu.solutis.dev.trail.locadora.exception.aluguel;

public class AluguelNotConfirmedException extends RuntimeException {
    public AluguelNotConfirmedException(Long id) {
        super("Rent with ID " + id + " not confirmed.");
    }
}
