package br.edu.solutis.dev.trail.locadora.model.dto.carro;

import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
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

    @NotNull(message = "Descição é obrigatória")
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 1, max = 255, message = "Descrição deve ter entre 1 e 255 caracteres")
    private String description;

    @NotNull(message = "Categoria é obrigatória")
    @Enumerated(EnumType.STRING)
    private ModeloCategoriaEnum category;

    @NotNull(message = "Fabricante é obrigatório")
    private Long manufacturerId;
}