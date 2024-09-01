
package br.edu.solutis.dev.trail.locadora.repository.aluguel;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Aluguel;

import java.util.List;

@Repository

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    List<Aluguel> findByFinalizadoTrue();
    List<Aluguel> findByFinalizadoFalse();

}






