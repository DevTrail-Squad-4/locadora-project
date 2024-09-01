package br.edu.solutis.dev.trail.locadora.exception.aluguel;

public class AluguelNotFoundException extends RuntimeException {
    public AluguelNotFoundException(Long id) {
        super("Rent with ID " + id + " not found.");
    }
}