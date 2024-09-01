package br.edu.solutis.dev.trail.locadora.exception.aluguel;


public class AluguelAlreadyConfirmedException extends RuntimeException {
    public AluguelAlreadyConfirmedException(Long id) {
        super("Rent with ID " + id + " is already confirmed.");
    }
}
