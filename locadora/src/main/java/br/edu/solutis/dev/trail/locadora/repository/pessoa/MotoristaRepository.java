package br.edu.solutis.dev.trail.locadora.repository.pessoa;

import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.Motorista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista,Long> {
    Optional<Motorista> findByEmail(String email);
    Page<Motorista> findByDeletedFalse(Pageable pageable);
    Optional<Motorista> findByCpf(String cpf);

}
