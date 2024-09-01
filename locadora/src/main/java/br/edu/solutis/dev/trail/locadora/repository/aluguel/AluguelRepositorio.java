
package br.edu.solutis.dev.trail.locadora.repository.aluguel;


import br.edu.solutis.dev.trail.locadora.model.entity.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AluguelRepositorio extends JpaRepository<Aluguel, Long> {
    List<Aluguel> findByFinalizado();
    List<Aluguel> findByNaoFinalizado();

}






