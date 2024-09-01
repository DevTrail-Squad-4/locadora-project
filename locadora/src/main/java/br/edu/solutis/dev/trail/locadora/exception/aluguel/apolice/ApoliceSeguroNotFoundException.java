package br.edu.solutis.dev.trail.locadora.exception.aluguel.insurace;

public class ApoliceSeguroNotFoundException extends RuntimeException {
    public ApoliceSeguroNotFoundException(Long id) {
        super("Insurance Policy with ID " + id + " not found.");
    }
}