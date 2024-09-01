package br.edu.solutis.dev.trail.locadora.model.dto.aluguel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class AluguelDto {
    private Long id;

    @NotNull(message = "Inserção de data inicial obrigatória")
    private LocalDate dataInicial;

    @NotNull(message = "Inserção de data final obrigatória")
    private LocalDate dataFinal;

    private LocalDate dataFinalizada;

    private BigDecimal valor;

    @NotNull(message = "Concordacia à apolice de seguro requerida")
    private long apoliceSeguroId;

    private long motoristaId;

    @NotNull(message = "Necessita ter um carro escolhido")
    private long carroId;

    private long carrinhoId;
}
