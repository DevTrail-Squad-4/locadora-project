package br.com.solutis.locadora.model.dto.car;

import br.com.solutis.locadora.model.entity.car.ModelCategoryEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ModeloDtoResponse {

    private Long id;
    private String modelo;
    private ModelCategoryEnum categoria;
    private String fabricante;

}