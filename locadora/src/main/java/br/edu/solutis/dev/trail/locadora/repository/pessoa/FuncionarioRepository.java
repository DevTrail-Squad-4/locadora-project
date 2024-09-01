package br.edu.solutis.dev.trail.locadora.repository.pessoa;

import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Page<Funcionario> findByDeletedFalse(Pageable pageable);
}
