package br.edu.solutis.dev.trail.locadora.model.entity.carro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Aluguel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carros")
public class Carro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String plate;

    @Column(unique = true, nullable = false)
    private String chassis;

    @Column(nullable = false)
    private String color;

    @Column(name = "valor_diario", nullable = false)
    private BigDecimal valorDiario;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private boolean alugado = false;

    @JsonIgnoreProperties("cars")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_id", nullable = false)
    private Modelo model;

    @JsonIgnoreProperties("cars")
    @ManyToMany
    @JoinTable(
            name = "car_accessories",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "accessory_id")
    )
    private List<Acessorio> accessories;

    @JsonIgnoreProperties("cars")
    @OneToMany(mappedBy = "car")
    private List<Aluguel> rents;

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
        return "Carro{" +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", chassis='" + chassis + '\'' +
                ", color='" + color + '\'' +
                ", dailyValue=" + valorDiario +
                ", imageUrl='" + imageUrl + '\'' +
                ", rented=" + alugado +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +

                '}';
    }
}