package br.com.solutis.locadora.repository.carro;

import br.com.solutis.locadora.model.entity.car.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Page<Manufacturer> findByDeletedFalse(Pageable pageable);
