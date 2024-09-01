package br.edu.solutis.dev.trail.locadora.model.entity.aluguel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "insurance_policies")
public class ApoliceSeguro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Valor_franquia", nullable = false)
    private BigDecimal valorFranquia;

    @Column(name = "protecao_terceiro")
    private boolean protecaoTerceiro = false;

    @Column(name = "protecao_causas_naturais")
    private boolean protecaoCausasNaturais = false;

    @Column(name = "seguro_roubo")
    private boolean protecaoRoubo = false;

    @JsonIgnoreProperties("apoliceSeguro")
    @OneToMany(mappedBy = "apoliceSeguro")
    private List<Aluguel> aluguelCarros;

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
        return "ApoliceSeguro{" +
                "id=" + id +
                ", valorFranduia=" + valorFranquia +
                ", protecaoTerceiro=" + protecaoTerceiro +
                ", protecaoCausasNaturais=" + protecaoCausasNaturais +
                ", protecaoRoubo=" + protecaoRoubo +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}