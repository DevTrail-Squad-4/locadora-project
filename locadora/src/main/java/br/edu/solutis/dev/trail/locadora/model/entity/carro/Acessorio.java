package br.edu.solutis.dev.trail.locadora.model.entity.carro;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "acessorios")
public class Acessorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String description;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "car_accessories",
            joinColumns = @JoinColumn(name = "accessory_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id")
    )
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
        return "Acessorio{" +
                "id=" + id +
                ", descrição='" + description + '\'' +
                ", excluido=" + deleted +
                ", criadoEm=" + createdAt +
                ", atualizadoEm=" + updatedAt +
                '}';
    }
}