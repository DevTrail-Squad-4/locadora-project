package br.com.solutis.locadora.service.person;

import br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista.MotoristaException;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista.MotoristaNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.MotoristaDTO;
import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.MotoristaEntity;
import br.edu.solutis.dev.trail.locadora.repository.pessoa.MotoristaRepository;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.service.CrudService;
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
public class MotoristaService implements CrudService<MotoristaDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotoristaService.class);
    private final MotoristaRepository driverRepository;
    private final GenericMapper<MotoristaDTO, MotoristaEntity> modelMapper;

    public MotoristaDTO findById(Long id) {
        LOGGER.info("Encontrando motorista com o id: {}", id);
        MotoristaEntity driver = getDriver(id);

        return modelMapper.mapModelToDto(driver, MotoristaDTO.class);
    }

    public PageResponse<MotoristaDTO> findAll(int pageNo, int pageSize) {
        LOGGER.info("Fetching drivers with page number {} and page size {}.", pageNo, pageSize);

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<MotoristaEntity> pagedDrivers = driverRepository.findByDeletedFalse(paging);

            List<MotoristaDTO> driverDtos = modelMapper
                    .mapList(pagedDrivers.getContent(), MotoristaDTO.class);

            PageResponse<MotoristaDTO> pageResponse = new PageResponse<>();
            pageResponse.setContent(driverDtos);
            pageResponse.setCurrentPage(pagedDrivers.getNumber());
            pageResponse.setTotalItems(pagedDrivers.getTotalElements());
            pageResponse.setTotalPages(pagedDrivers.getTotalPages());

            return pageResponse;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new MotoristaException("Um erro ocorreu durante o carregamento de motoristas.", e);
        }
    }

    public MotoristaDTO add(MotoristaDTO payload) {
        try {
            LOGGER.info("Salvando motorista: {}", payload);

            MotoristaEntity driver = driverRepository
                    .save(modelMapper.mapDtoToModel(payload, MotoristaEntity.class));

            return modelMapper.mapModelToDto(driver, MotoristaDTO.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new MotoristaException("Um erro ocorreu ao salvar o motorista.", e);
        }
    }

    public MotoristaDTO update(MotoristaDTO payload) {
        MotoristaEntity existingDriver = getDriver(payload.getId());
        if (existingDriver.isDeleted()) throw new MotoristaNotFoundException(existingDriver.getId());

        try {
            LOGGER.info("Editando motorista: {}", payload);
            MotoristaDTO driverDto = modelMapper
                    .mapModelToDto(existingDriver, MotoristaDTO.class);

            updateDriverFields(payload, driverDto);

            MotoristaEntity driver = driverRepository
                    .save(modelMapper.mapDtoToModel(driverDto, MotoristaEntity.class));

            return modelMapper.mapModelToDto(driver, MotoristaDTO.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new MotoristaException("Um erro ocorreu ao editar o motorista", e);
        }
    }

    public void deleteById(Long id) {
        MotoristaDTO driverDto = findById(id);

        try {
            LOGGER.info("Soft deleting driver with ID: {}", id);


            MotoristaEntity driver = modelMapper.mapDtoToModel(driverDto, MotoristaEntity.class);
            driver.setDeleted(true);

            driverRepository.save(driver);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new MotoristaException("Um erro ocorreu ao deleter o usuario", e);
        }
    }

    private void updateDriverFields(MotoristaDTO payload, MotoristaDTO existingDriver) {
        if (payload.getNome() != null) {
            existingDriver.setNome(payload.getNome());
        }
        if (payload.getCNH() != null) {
            existingDriver.setCnh(payload.getCnh());
        }
        if (payload.getAniversario() != null) {
            existingDriver.setAniversario(payload.getAniversario());
        }
        if (payload.getSexo() != null) {
            existingDriver.setSexo(payload.getSexo());
        }
    }

    private MotoristaEntity getDriver(Long id) {
        return driverRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Motorista com o id {} não encontrado.", id);
            return new MotoristaNotFoundException(id);
        });
    }
}
