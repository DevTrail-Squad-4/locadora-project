package br.edu.solutis.dev.trail.locadora.exception.carro;

public class CarroAlreadyRentedException extends RuntimeException {
    public CarroAlreadyRentedException(Long id) {
        super("Carro " + id + " já foi alugado.");
    }
}