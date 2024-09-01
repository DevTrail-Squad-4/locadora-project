package br.edu.solutis.dev.trail.locadora.repository.aluguel;

import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Carrinho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    Page<Carrinho> findByDeletedFalse(Pageable pageable);

    Carrinho findByMotoristaId(Long driverId);

}
