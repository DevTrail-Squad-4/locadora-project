package br.edu.solutis.dev.trail.locadora.repository.carro;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Modelo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    Page<Modelo> findByDeletedFalse(Pageable pageable);
}