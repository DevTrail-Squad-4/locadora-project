package br.edu.solutis.dev.trail.locadora.repository.carro;

import br.edu.solutis.dev.trail.locadora.model.entity.carro.Acessorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcessorioRepository extends JpaRepository<Acessorio, Long> {
    Page<Acessorio> findByDeletedFalse(Pageable pageable);
}