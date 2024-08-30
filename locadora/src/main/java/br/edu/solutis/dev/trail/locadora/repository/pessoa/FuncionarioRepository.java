package br.edu.solutis.dev.trail.locadora.repository.pessoa;

import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.FuncionarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<FuncionarioEntity, Long> {
    Page<FuncionarioEntity> findByDeletedFalse(Pageable pageable);
}
