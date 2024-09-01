package br.edu.solutis.dev.trail.locadora.model.dto.aluguel;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AluguelDtoResponse {

    
    private Long id;

    private String motoristaNome;

    private String cpf;

    private LocalDate dataInicial;

    private LocalDate dataFinal;

    private LocalDate dataTermino;

    private BigDecimal valor;

    private boolean confirmado;

    private boolean finalizado;

    private BigDecimal franquiaValor;

    private boolean coberturaTerceiros;

    private boolean coberturaFenomenos;

    private boolean coberturaRoubo;

    private String placa;

    private String cor;

    private String modelo;

    private ModeloCategoriaEnum categoria;

    private String fabricante;



}
