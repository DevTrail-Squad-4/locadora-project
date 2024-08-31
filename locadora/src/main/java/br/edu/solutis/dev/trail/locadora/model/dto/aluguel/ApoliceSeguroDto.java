package br.edu.solutis.dev.trail.locadora.model.dto.aluguel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class ApoliceSeguroDto {
    private Long id;

    @NotNull(message = "Valor da franquia é requerido")
    private BigDecimal valorFranquia;

    private boolean protecaoTerceiro = false;

    private boolean protecaoCausasNaturais = false;

    private boolean protecaoRoubo = false;
    
}