package br.edu.solutis.dev.trail.locadora.repository.aluguel;

import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.ApoliceSeguro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApoliceSeguroRepository extends JpaRepository<ApoliceSeguro, Long> {
    Page<ApoliceSeguro> findByDeletedFalse(Pageable pageable);
}



