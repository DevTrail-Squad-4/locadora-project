package br.edu.solutis.dev.trail.locadora.model.entity.pessoa;

import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Carrinho;
import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Aluguel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.util.List;



@Table(name = "motoristas")
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Motorista  extends Pessoa{

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cnh;

    @JsonIgnoreProperties("motoristas")
    @OneToMany(mappedBy = "motorista")
    private List<Aluguel> alugueis;

    @JsonIgnoreProperties("motoristas")
    @OneToOne(mappedBy = "motorista")
    private Carrinho carrinho;


    @Override
    public String toString() {
        return "Motorista{" +
                "cnh='" + cnh + '\'' +
                "} " + super.toString();
    }
}
