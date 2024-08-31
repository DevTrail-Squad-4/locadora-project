package br.edu.solutis.dev.trail.locadora.repository;

import br.edu.solutis.dev.trail.locadora.model.entity.carro.Acessorio;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Carro;
import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
    Page<Carro> findByDeletedFalseAndRentedFalse(Pageable pageable);

    @Query("SELECT DISTINCT c FROM Car c " +
            "LEFT JOIN c.rents r " +
            "WHERE (:rented IS NULL OR c.rented = :rented) " +
            "AND (:accessory IS NULL OR :accessory MEMBER OF c.accessories) " +
            "AND (c.deleted = false) " +
            "AND (:category IS NULL OR c.model.category = :category) " +
            "AND ((:rented = true) OR (c.rented = false))"+
            "AND (:model IS NULL OR c.model.description = :model)")

    List<Carro> findCarsByFilters(
            @Param("category") ModeloCategoriaEnum category,
            @Param("accessory") Acessorio accessory,
            @Param("model") String model,
            @Param("rented") Boolean rented);
}