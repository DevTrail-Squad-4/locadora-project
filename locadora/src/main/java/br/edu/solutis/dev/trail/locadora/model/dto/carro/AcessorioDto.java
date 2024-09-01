package br.edu.solutis.dev.trail.locadora.model.dto.carro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AcessorioDto {
    private Long id;

    @NotNull(message = "Descrição é obrigatória")
    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 1, max = 255, message = "Descrição deve ter entre 1 e 255 caracteres")
    private String description;
}