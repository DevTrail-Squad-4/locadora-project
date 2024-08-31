package br.edu.repository.aluguel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CarrinhoRepositorio extends JpaRepository<Cart, Long> {

    Page<Cart> findByDeletedFalse(Pageable pageable);

    Cart findByDriverId(Long driverId);

}
