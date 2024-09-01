package br.edu.solutis.dev.trail.locadora.service.carro;

import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.service.CrudService;
import br.edu.solutis.dev.trail.locadora.exception.carro.Fabricante.FabricanteException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Fabricante.FabricanteNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.FabricanteDto;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Fabricante;
import br.edu.solutis.dev.trail.locadora.repository.carro.FabricanteRepository;
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
public class FabricanteService implements CrudService<FabricanteDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FabricanteService.class);
    private final FabricanteRepository manufacturerRepository;
    private final GenericMapper<FabricanteDto, Fabricante> modelMapper;

    public FabricanteDto findById(Long id) {
        LOGGER.info("Localizando fabricante com ID {}", id);
        Fabricante manufacturer = getManufacturer(id);

        return modelMapper.mapModelToDto(manufacturer, FabricanteDto.class);
    }

    public PageResponse<FabricanteDto> findAll(int pageNo, int pageSize) {
        try {
            LOGGER.info("Buscando fabricante com número de página {} e tamanho de página {}.", pageNo, pageSize);

            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<Fabricante> pagedManufacturers = manufacturerRepository.findByDeletedFalse(paging);

            List<FabricanteDto> manufacturerDtos = modelMapper
                    .mapList(pagedManufacturers.getContent(), FabricanteDto.class);

            PageResponse<FabricanteDto> pageResponse = new PageResponse<>();
            pageResponse.setContent(manufacturerDtos);
            pageResponse.setCurrentPage(pagedManufacturers.getNumber());
            pageResponse.setTotalItems(pagedManufacturers.getTotalElements());
            pageResponse.setTotalPages(pagedManufacturers.getTotalPages());

            return pageResponse;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FabricanteException("Ocorreu um erro ao buscar os fabricantes.", e);
        }
    }

    public FabricanteDto add(FabricanteDto payload) {
        try {
            LOGGER.info("Adding manufacturer: {}", payload);

            Fabricante manufacturer = manufacturerRepository
                    .save(modelMapper.mapDtoToModel(payload, Fabricante.class));

            return modelMapper.mapModelToDto(manufacturer, FabricanteDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FabricanteException("Ocorreu um erro ao adicionar o fabricante.", e);
        }
    }

    public FabricanteDto update(FabricanteDto payload) {
        Fabricante existingManufacturer = getManufacturer(payload.getId());
        if (existingManufacturer.isDeleted()) throw new FabricanteNotFoundException(existingManufacturer.getId());

        try {
            LOGGER.info("Atualizando fabricante {}", payload);
            FabricanteDto manufacturerDto = modelMapper
                    .mapModelToDto(existingManufacturer, FabricanteDto.class);

            updateModelFields(payload, manufacturerDto);

            Fabricante manufacturer = manufacturerRepository
                    .save(modelMapper.mapDtoToModel(manufacturerDto, Fabricante.class));

            return modelMapper.mapModelToDto(manufacturer, FabricanteDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FabricanteException("Ocorreu um erro ao atualizar o fabricante.", e);
        }
    }

    public void deleteById(Long id) {
        FabricanteDto manufacturerDto = findById(id);

        try {
            LOGGER.info("Exclusão reversível do fabricante com ID {}", id);

            Fabricante manufacturer = modelMapper.mapDtoToModel(manufacturerDto, Fabricante.class);
            manufacturer.setDeleted(true);

            manufacturerRepository.save(manufacturer);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FabricanteException("Ocorreu um erro ao excluir o fabricante.", e);
        }
    }

    private void updateModelFields(FabricanteDto payload, FabricanteDto existingManufacturer) {
        if (payload.getName() != null) {
            existingManufacturer.setName(payload.getName());
        }
    }

    private Fabricante getManufacturer(Long id) {
        return manufacturerRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Fabricante com ID {} não encontrado.", id);
                    return new FabricanteNotFoundException(id);
                });
    }
}