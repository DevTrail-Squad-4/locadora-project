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
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ModeloCategoriaEnum category;

    @JsonIgnoreProperties("modelos")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fabricante_id", nullable = false)
    private Fabricante fabricante;

    @JsonIgnoreProperties("modelos")
    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private List<Carro> cars;

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
                ", descrição='" + description + '\'' +
                ", categoria=" + category +
                ", excluido=" + deleted +
                ", criadoEm=" + createdAt +
                ", atualizadoEm=" + updatedAt +
                '}';
    }
}