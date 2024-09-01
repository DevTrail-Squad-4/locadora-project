package br.edu.solutis.dev.trail.locadora.service.carro;

import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.service.CrudService;
import br.edu.solutis.dev.trail.locadora.exception.carro.Acessorio.AcessorioException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Acessorio.AcessorioNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.AcessorioDto;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Acessorio;
import br.edu.solutis.dev.trail.locadora.repository.carro.AcessorioRepository;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AcessorioService implements CrudService<AcessorioDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarroService.class);
    private final AcessorioRepository accessoryRepository;
    private final GenericMapper<AcessorioDto, Acessorio> modelMapper;

    public AcessorioDto findById(Long id) {
        LOGGER.info("Encontrar acessório com ID {}", id);
        Acessorio accessory = getAccessory(id);

        return modelMapper.mapModelToDto(accessory, AcessorioDto.class);
    }

    public PageResponse<AcessorioDto> findAll(int pageNo, int pageSize) throws AcessorioNotFoundException{

            LOGGER.info("Buscando acessórios com número de página {} e tamanho de página {}.", pageNo, pageSize);
            try{
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<Acessorio> pagedAccessories = accessoryRepository.findAll(paging);
            List<AcessorioDto> accessoryDtos = modelMapper.
                    mapList(pagedAccessories.getContent(), AcessorioDto.class);

            PageResponse<AcessorioDto> pageResponse = new PageResponse<>();
            pageResponse.setContent(accessoryDtos);
            pageResponse.setCurrentPage(pagedAccessories.getNumber());
            pageResponse.setTotalItems(pagedAccessories.getTotalElements());
            pageResponse.setTotalPages(pagedAccessories.getTotalPages());

            return pageResponse;
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw new AcessorioException("Ocorreu um erro ao encontrar o acessório.", e);
            }
    }

    public AcessorioDto add(AcessorioDto payload) throws AcessorioException{

        try{
            LOGGER.info("Adicionando acessório {}.", payload);

            Acessorio accessory = accessoryRepository
                    .save(modelMapper.mapDtoToModel(payload, Acessorio.class));

            return modelMapper.mapModelToDto(accessory, AcessorioDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new AcessorioException("Ocorreu um erro ao adicionar acessório.", e);
        }
    }

    public AcessorioDto update(AcessorioDto payload) throws AcessorioException{
        Acessorio existingAccessory = getAccessory(payload.getId());
        if (existingAccessory.isDeleted()) throw new AcessorioNotFoundException(existingAccessory.getId());

        try{


            LOGGER.info("Atualizando acessório {}.", payload);
            AcessorioDto accessoryDto = modelMapper
                    .mapModelToDto(existingAccessory, AcessorioDto.class);

            updateAccessoryFields(payload, accessoryDto);

            Acessorio accessory = accessoryRepository
                    .save(modelMapper.mapDtoToModel(accessoryDto, Acessorio.class));

            return modelMapper.mapModelToDto(accessory, AcessorioDto.class);

        } catch (Exception e) {
        LOGGER.error(e.getMessage());
        throw new AcessorioException("Ocorreu um erro ao atualizar o acessório.", e);
        }
    }

    public void deleteById(Long id) {
        AcessorioDto accessoryDto = findById(id);

        try {
            LOGGER.info("Acessório de exclusão reversível com ID {}.", id);

            Acessorio accessory = modelMapper.mapDtoToModel(accessoryDto, Acessorio.class);
            accessory.setDeleted(true);

            accessoryRepository.save(accessory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new AcessorioException("Ocorreu um erro ao excluir o acessório.", e);
        }
    }

    public void updateAccessoryFields(AcessorioDto payload, AcessorioDto existingAccessory) {
        if (payload.getDescription() != null) {
            existingAccessory.setDescription(payload.getDescription());
        }
    }

    private Acessorio getAccessory(Long id) {
        return accessoryRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Acessório com ID {} não encontrado.", id);
            return new AcessorioNotFoundException(id);
        });
    }
}