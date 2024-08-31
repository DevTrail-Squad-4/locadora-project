package br.edu.solutis.dev.trail.locadora.model.entity.carro;

//import br.com.solutis.locadora.model.entity.rent.Rent;
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

    @Column(name = "daily_value", nullable = false)
    private BigDecimal dailyValue;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private boolean rented = false;

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

    //@JsonIgnoreProperties("cars")
    //@OneToMany(mappedBy = "car")
    //private List<Aluguel> rents;

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
        return "Car{" +
                "id=" + id +
                ", plate='" + plate + '\'' +
                ", chassis='" + chassis + '\'' +
                ", color='" + color + '\'' +
                ", dailyValue=" + dailyValue +
                ", imageUrl='" + imageUrl + '\'' +
                ", rented=" + rented +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}