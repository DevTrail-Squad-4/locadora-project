package br.edu.repository.aluguel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CarrinhoRepositorio extends JpaRepository<Carrinho, Long> {

    Page<Cart> findByDeletedFalse(Pageable pageable);

    Carrinho findByDriverId(Long driverId);

}
