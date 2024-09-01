package br.edu.solutis.dev.trail.locadora.model.dto.carro;

import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ModeloDtoResponse {

    private Long id;
    private String model;
    private ModeloCategoriaEnum category;
    private String manufacturer;

}