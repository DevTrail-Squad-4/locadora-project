package br.edu.solutis.dev.trail.locadora.exception.carro;

public class CarroNotFoundException extends RuntimeException {
    public CarroNotFoundException(Long id) {
        super("Carro " + id + " Não Encontrado.");
    }
}