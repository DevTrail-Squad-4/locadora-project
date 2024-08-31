package br.com.solutis.locadora.model.dto.car;

import br.com.solutis.locadora.model.entity.car.ModelCategoryEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ModeloDto {
    private Long id;

    @NotNull(message = "A descrição é obrigatória")
    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 1, max = 255, message = "A descrição deve ter entre 1 e 255 caracteres")
    private String descrição;

    @NotNull(message = "A descrição é obrigatória")
    @Enumerated(EnumType.STRING)
    private ModelCategoryEnum categoria;

    @NotNull(message = "O fabricante é obrigatório")
    private Long fabricante;