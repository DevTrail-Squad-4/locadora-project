package br.edu.solutis.dev.trail.locadora.exception.carro;

public class CarroJaAlugadoException extends RuntimeException {
    public CarroJaAlugadoException(Long id) {
        super("Carro " + id + " já foi alugado.");
    }
}