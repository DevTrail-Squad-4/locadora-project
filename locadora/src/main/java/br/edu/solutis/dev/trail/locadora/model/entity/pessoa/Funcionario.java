package br.edu.solutis.dev.trail.locadora.model.entity.pessoa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table(name = "Funcionario")
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Funcionario extends Pessoa{
    @Column(unique = true, nullable = false)
    private String registration;

    @Override
    public String toString() {
        return "Employee{" +
                "registration='" + registration + '\'' +
                "} " + super.toString();
    }
}
