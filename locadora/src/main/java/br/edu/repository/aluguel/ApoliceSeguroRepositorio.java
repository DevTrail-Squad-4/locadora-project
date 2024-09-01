package br.edu.repository.aluguel;


package br.com.solutis.locadora.repository.rent;

import br.com.solutis.locadora.model.entity.rent.InsurancePolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public class ApoliceSeguroRepositorio extends JpaRepository<InsurancePolicy, Long> {
    Page<InsurancePolicy> findByDeletedFalse(Pageable pageable);
}



