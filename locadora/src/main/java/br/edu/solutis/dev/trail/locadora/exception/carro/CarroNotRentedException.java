package br.edu.solutis.dev.trail.locadora.exception.carro;

public class CarroNotRentedException extends RuntimeException {
    public CarroNotRentedException(Long id) {
        super("Carro " + id + " não está alugado.");
    }
}
