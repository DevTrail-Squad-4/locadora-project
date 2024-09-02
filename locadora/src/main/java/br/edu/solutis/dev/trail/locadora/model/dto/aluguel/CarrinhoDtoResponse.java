package br.edu.solutis.dev.trail.locadora.model.dto.aluguel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Aluguel;

@Data
@EqualsAndHashCode
public class CarrinhoDtoResponse {

    private Long id;
    private String nome;
    private String cpf;
    private List<Aluguel> alugueis;
}

