package br.edu.solutis.dev.trail.locadora.repository.person;

import br.edu.solutis.dev.trail.locadora.model.entity.MotoristaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<MotoristaEntity,Long> {
    Page<MotoristaEntity> findByDeletedFalse(Pageable pageable);
}
