package br.edu.solutis.dev.trail.locadora.exception.carro.Fabricante;

public class FabricanteNotFoundException extends RuntimeException {
    public FabricanteNotFoundException(Long id) {
        super("Fabricante " + id + " não encontrado.");
    }
}