package br.edu.solutis.dev.trail.locadora.service.carro;

import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.service.CrudService;
import br.edu.solutis.dev.trail.locadora.exception.carro.Acessorio.AcessorioException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Acessorio.AcessorioNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.AcessorioDto;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Acessorio;
import br.edu.solutis.dev.trail.locadora.repository.AcessorioRepository;
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
        LOGGER.info("Finding accessory with ID: {}", id);
        Acessorio accessory = getAccessory(id);

        return modelMapper.mapModelToDto(accessory, AcessorioDto.class);
    }

    public PageResponse<AcessorioDto> findAll(int pageNo, int pageSize) throws AcessorioNotFoundException{

            LOGGER.info("Fetching accessories with page number {} and page size {}.", pageNo, pageSize);
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
                throw new AcessorioException("An error occurred while finding accessory.", e);
            }
    }

    public AcessorioDto add(AcessorioDto payload) throws AcessorioException{

        try{
            LOGGER.info("Adding accessory {}.", payload);

            Acessorio accessory = accessoryRepository
                    .save(modelMapper.mapDtoToModel(payload, Acessorio.class));

            return modelMapper.mapModelToDto(accessory, AcessorioDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new AcessorioException("An error occurred while add accessory.", e);
        }
    }

    public AcessorioDto update(AcessorioDto payload) throws AcessorioException{
        Acessorio existingAccessory = getAccessory(payload.getId());
        if (existingAccessory.isDeleted()) throw new AcessorioNotFoundException(existingAccessory.getId());

        try{


            LOGGER.info("Updating accessory {}.", payload);
            AcessorioDto accessoryDto = modelMapper
                    .mapModelToDto(existingAccessory, AcessorioDto.class);

            updateAccessoryFields(payload, accessoryDto);

            Acessorio accessory = accessoryRepository
                    .save(modelMapper.mapDtoToModel(accessoryDto, Acessorio.class));

            return modelMapper.mapModelToDto(accessory, AcessorioDto.class);

        } catch (Exception e) {
        LOGGER.error(e.getMessage());
        throw new AcessorioException("An error occurred while updating accessory.", e);
        }
    }

    public void deleteById(Long id) {
        AcessorioDto accessoryDto = findById(id);

        try {
            LOGGER.info("Soft deleting accessory with ID {}.", id);

            Acessorio accessory = modelMapper.mapDtoToModel(accessoryDto, Acessorio.class);
            accessory.setDeleted(true);

            accessoryRepository.save(accessory);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new AcessorioException("An error occurred while deleting accessory.", e);
        }
    }

    public void updateAccessoryFields(AcessorioDto payload, AcessorioDto existingAccessory) {
        if (payload.getDescription() != null) {
            existingAccessory.setDescription(payload.getDescription());
        }
    }

    private Acessorio getAccessory(Long id) {
        return accessoryRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Accessory with ID {} not found.", id);
            return new AcessorioNotFoundException(id);
        });
    }
}