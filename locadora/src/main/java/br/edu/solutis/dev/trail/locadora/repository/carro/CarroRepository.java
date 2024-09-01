package br.edu.solutis.dev.trail.locadora.repository.carro;

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
    Page<Carro> findByDeletedFalseAndAlugadoFalse(Pageable pageable);

    @Query("SELECT DISTINCT c FROM Carro c " +
            "LEFT JOIN c.alugueis r " +
            "WHERE (:alugado IS NULL OR c.alugado = :alugado) " +
            "AND (:acessorio IS NULL OR :acessorio MEMBER OF c.acessorios) " +
            "AND (c.deleted = false) " +
            "AND (:categoria IS NULL OR c.modelo.categoria = :categoria) " +
            "AND ((:alugado = true) OR (c.alugado = false))"+
            "AND (:modelo IS NULL OR c.modelo.descricao = :modelo)")

    List<Carro> findCarrosByFilters(
            @Param("categoria") ModeloCategoriaEnum categoria,
            @Param("acessorio") Acessorio acessorio,
            @Param("modelo") String modelo,
            @Param("alugado") Boolean alugado);
}