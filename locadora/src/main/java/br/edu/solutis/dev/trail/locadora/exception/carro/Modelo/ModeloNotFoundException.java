package br.edu.solutis.dev.trail.locadora.exception.carro.Modelo;

public class ModeloNotFoundException extends RuntimeException {
    public ModeloNotFoundException(Long id) {
        super("Modelo " + id + " não encontrado.");
    }
}