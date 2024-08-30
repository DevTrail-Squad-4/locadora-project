package br.edu.solutis.dev.trail.locadora.repository.person;

import br.edu.solutis.dev.trail.locadora.model.entity.FuncionarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<FuncionarioEntity, Long> {
    Page<FuncionarioEntity> findByDeletedFalse(Pageable pageable);
}
