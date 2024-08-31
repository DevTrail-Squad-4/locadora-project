package br.edu.solutis.dev.trail.locadora.model.dto.carro;

import br.edu.solutis.dev.trail.locadora.model.entity.carro.Acessorio;
import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode
public class CarroDtoResponse {

        private Long id;

        private String imageUrl;

        private String description;

        private String color;

        private String name;

        private ModeloCategoriaEnum category;

        private String plate;

        private String chassis;

        private BigDecimal dailyValue;

        private boolean rented;

        private List<Acessorio> accessories;


}