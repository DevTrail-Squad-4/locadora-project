package br.edu.solutis.dev.trail.locadora.model.entity.pessoa;

import br.edu.solutis.dev.trail.locadora.model.enums.SexoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "deletado", nullable = false)
    private boolean deleted = false;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "editado_em", nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDate aniversario;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private SexoEnum sexo;

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
        return "Pessoa {" +
                "id =" + id +
                ", deletado =" + deleted +
                ", criado =" + createdAt +
                ", atualizaçao =" + updatedAt +
                ", nome ='" + nome + '\'' +
                ", cpf ='" + cpf + '\'' +
                ", aniversario =" + aniversario +
                ", sexo =" + sexo +
                '}';
    }
}

