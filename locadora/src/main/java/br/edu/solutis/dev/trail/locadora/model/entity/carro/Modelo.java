package br.edu.solutis.dev.trail.locadora.model.entity.carro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "modelos")
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private ModeloCategoriaEnum categoria;

    @JsonIgnoreProperties("modelos")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fabricante_id", nullable = false)
    private Fabricante fabricante;

    @JsonIgnoreProperties("modelos")
    @OneToMany(mappedBy = "modelo", fetch = FetchType.LAZY)
    private List<Carro> carros;

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
        return "Modelo{" +
                "id=" + id +
                ", descrição='" + descricao + '\'' +
                ", categoria=" + categoria +
                ", excluido=" + deleted +
                ", criadoEm=" + createdAt +
                ", atualizadoEm=" + updatedAt +
                '}';
    }
}