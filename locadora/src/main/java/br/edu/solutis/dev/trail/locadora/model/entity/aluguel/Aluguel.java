package br.edu.solutis.dev.trail.locadora.model.entity.aluguel;


import br.edu.solutis.dev.trail.locadora.model.entity.carro.Carro;
import  br.edu.solutis.dev.trail.locadora.model.entity.pessoa.Motorista;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "alugueis")
public class Aluguel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_inicial", nullable = false)
    private LocalDate dataInicial;

    @Column(name = "data_final", nullable = false)
    private LocalDate dataFinal;

    @Column(name = "data_finalizada")
    private LocalDate dataFinalizada;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private boolean confirmado = false;

    @Column(nullable = false)
    private boolean finalizado = false;

    @JsonIgnoreProperties("alugueis")
    @ManyToOne(optional = false)
    @JoinColumn(name = "apolice_seguro_id", nullable = false)
    private ApoliceSeguro apoliceSeguro;

    @JsonIgnoreProperties("alugueis")
    @ManyToOne(optional = false)
    @JoinColumn(name = "motorista_id", nullable = false)
    private Motorista motorista;

    @JsonIgnoreProperties("alugueis")
    @ManyToOne(optional = false)
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;

    @JsonIgnoreProperties("alugueis")
    @ManyToOne
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Aluguel{" +
                "id=" + id +
                ", dataFinalizada=" + dataFinalizada +
                ", dataInicial=" + dataInicial +
                ", dataFinal=" + dataFinal +
                ", valor=" + valor +
                ", confirmado=" + confirmado +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}