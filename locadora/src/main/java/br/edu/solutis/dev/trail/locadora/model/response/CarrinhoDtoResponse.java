package br.edu.solutis.dev.trail.locadora.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public class CarrinhoDtoResponse {


@Data
@EqualsAndHashCode
public class CartDtoResponse {

    private Long id;
    private String nome;
    private String cpf;
    private List<Aluguel> aluguel;
}
}
