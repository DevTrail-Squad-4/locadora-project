package br.edu.solutis.dev.trail.locadora.model.dto.aluguel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class CarrinhoDto {

    private long id;

    @NotNull(message = "Motorista necessário")
    
    private long idMotorista;
    
    private List<Long> IdDosAlugueis;

    

}