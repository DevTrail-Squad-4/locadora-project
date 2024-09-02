package br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista;

public class MotoristaEmailNotFoundException extends RuntimeException {
    public MotoristaEmailNotFoundException(String email) {
        super("Motorista com o email " + email + " não encontrado.");
    }
}
