package br.edu.repository.aluguel;

package br.com.solutis.locadora.repository.rent;


import br.com.solutis.locadora.model.entity.rent.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public class AluguelRepositorio extends JpaRepository<Rent, Long> {
    List<Rent> findByFinishedTrue();
    List<Rent> findByFinishedFalse();

}






