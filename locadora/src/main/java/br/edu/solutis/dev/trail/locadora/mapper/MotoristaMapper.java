package br.edu.solutis.dev.trail.locadora.mapper;

import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.MotoristaDTO;
import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.Motorista;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MotoristaMapper {

    MotoristaMapper INSTANCE = Mappers.getMapper(MotoristaMapper.class);

    // Mapeia Motorista para MotoristaDTO
    @Mapping(source = "aniversario", target = "aniversario")
    MotoristaDTO modelToDTO(Motorista motorista);

    // Mapeia MotoristaDTO para Motorista
    @Mapping(source = "aniversario", target = "aniversario")
    Motorista dtoToModel(MotoristaDTO motoristaDTO);
}
