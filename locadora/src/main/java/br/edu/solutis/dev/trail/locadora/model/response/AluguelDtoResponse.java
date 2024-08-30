package br.edu.solutis.dev.trail.locadora.model.reponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AluguelDtoResponse {

    
    private Long id;

    private String carroNome;

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

    private ModelCategoryEnum categoria;

    private String fabricantes;



}
