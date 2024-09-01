package br.edu.solutis.dev.trail.locadora.model.dto.carro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode
public class CarroDto {
    private Long id;

    @NotNull(message = "Placa é obrigatória")
    @NotBlank(message = "Placa é obrigatória")
    @Size(min = 1, max = 255, message = "Placa deve ter entre 1 e 255 caracteres")
    private String plate;

    @NotNull(message = "Chassi é obrigatório")
    @NotBlank(message = "Chassi é obrigatório")
    @Size(min = 1, max = 255, message = "Chassi deve ter entre 1 e 255 caracteres")
    private String chassis;

    @NotNull(message = "Cor é obrigatória")
    @NotBlank(message = "Cor é obrigatória")
    private String color;

    @NotNull(message = "O valor diário é obrigatório")
    private BigDecimal dailyValue;

    private boolean alugado = false;

    @NotNull(message = "Url da imagem é obrigatória")
    @NotBlank(message = "Url da imagem é obrigatória")
    @URL(message = "Url da imagem inválida")
    private String imageUrl;

    @NotNull(message = "Modelo é obrigatório")
    private Long modelId;

    private List<Long> accessoriesIds;
}