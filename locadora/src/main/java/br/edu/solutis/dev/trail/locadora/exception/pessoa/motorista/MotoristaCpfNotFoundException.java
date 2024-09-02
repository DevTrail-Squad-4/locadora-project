package br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista;

public class MotoristaCpfNotFoundException extends RuntimeException {
    public MotoristaCpfNotFoundException(String cpf) {
        super("Motorista com o CPF " + cpf + " não encontrado.");
    }
}
