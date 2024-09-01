package br.edu.solutis.dev.trail.locadora.model.dto.aluguel;

import br.edu.solutis.dev.trail.locadora.model.entity.Aluguel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class CarrinhoDtoResponse {

    private Long id;
    private String nome;
    private String cpf;
    private List<Aluguel> aluguel;
}

