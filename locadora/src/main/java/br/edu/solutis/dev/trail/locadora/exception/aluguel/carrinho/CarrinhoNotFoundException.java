package br.edu.solutis.dev.trail.locadora.exception.aluguel.carrinho;


public class CarrinhoNotFoundException extends RuntimeException {
    public CarrinhoNotFoundException(Long id) {
        super("Cart with ID " + id + " not found.");
    }
}