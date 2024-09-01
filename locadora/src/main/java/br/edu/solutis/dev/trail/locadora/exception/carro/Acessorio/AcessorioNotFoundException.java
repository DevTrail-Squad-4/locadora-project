package br.edu.solutis.dev.trail.locadora.exception.carro.Acessorio;

public class AcessorioNotFoundException extends RuntimeException {
    public AcessorioNotFoundException(Long id) {
        super("Acessorio " + id + " não encontrado.");
    }
}