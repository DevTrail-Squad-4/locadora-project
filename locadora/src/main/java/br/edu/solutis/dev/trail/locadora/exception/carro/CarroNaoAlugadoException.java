package br.edu.solutis.dev.trail.locadora.exception.carro;

public class CarroNaoAlugadoException extends RuntimeException {
    public CarroNaoAlugadoException(Long id) {
        super("Carro " + id + " não está alugado.");
    }
}
