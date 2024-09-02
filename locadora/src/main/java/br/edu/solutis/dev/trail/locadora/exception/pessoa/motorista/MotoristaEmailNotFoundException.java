package br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista;

public class MotoristaEmailNotFoundException extends RuntimeException {
    public MotoristaEmailNotFoundException(String email) {
        super("O email: " + email + " nao foi encontrado.");
    }
}
