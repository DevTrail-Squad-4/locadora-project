package br.com.solutis.locadora.service.person;

import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.MotoristaEntity;
import br.edu.solutis.dev.trail.locadora.repository.pessoa.MotoristaRepository;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
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
public class DriverService implements CrudService<MotoristaDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverService.class);
    private final MotoristaRepository driverRepository;
    private final GenericMapper<DriverDto, MotoristaEntity> modelMapper;

    public MotoristaDto findById(Long id) {
        LOGGER.info("Finding driver with ID: {}", id);
        MotoristaEntity driver = getDriver(id);

        return modelMapper.mapModelToDto(driver, MotoristaDto.class);
    }

    public PageResponse<MotoristaDto> findAll(int pageNo, int pageSize) {
        LOGGER.info("Fetching drivers with page number {} and page size {}.", pageNo, pageSize);

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<MotoristaEntity> pagedDrivers = driverRepository.findByDeletedFalse(paging);

            List<MotoristaDto> driverDtos = modelMapper
                    .mapList(pagedDrivers.getContent(), MotoristaDto.class);

            PageResponse<MotoristaDto> pageResponse = new PageResponse<>();
            pageResponse.setContent(driverDtos);
            pageResponse.setCurrentPage(pagedDrivers.getNumber());
            pageResponse.setTotalItems(pagedDrivers.getTotalElements());
            pageResponse.setTotalPages(pagedDrivers.getTotalPages());

            return pageResponse;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new DriverException("An error occurred while fetching drivers.", e);
        }
    }

    public MotoristaDto add(MotoristaDto payload) {
        try {
            LOGGER.info("Adding driver: {}", payload);

            MotoristaEntity driver = driverRepository
                    .save(modelMapper.mapDtoToModel(payload, MotoristaEntity.class));

            return modelMapper.mapModelToDto(driver, MotoristaDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new DriverException("An error occurred while adding driver.", e);
        }
    }

    public MotoristaDto update(MotoristaDto payload) {
        MotoristaEntity existingDriver = getDriver(payload.getId());
        if (existingDriver.isDeleted()) throw new DriverNotFoundException(existingDriver.getId());

        try {
            LOGGER.info("Updating driver: {}", payload);
            MotoristaDto driverDto = modelMapper
                    .mapModelToDto(existingDriver, MotoristaDto.class);

            updateDriverFields(payload, driverDto);

            MotoristaEntity driver = driverRepository
                    .save(modelMapper.mapDtoToModel(driverDto, MotoristaEntity.class));

            return modelMapper.mapModelToDto(driver, DriverDto.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new DriverException("An error occurred while updating driver.", e);
        }
    }

    public void deleteById(Long id) {
        MotoristaDto driverDto = findById(id);

        try {
            LOGGER.info("Soft deleting driver with ID: {}", id);


            MotoristaEntity driver = modelMapper.mapDtoToModel(driverDto, MotoristaEntity.class);
            driver.setDeleted(true);

            driverRepository.save(driver);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new DriverException("An error occurred while deleting driver.", e);
        }
    }

    private void updateDriverFields(MotoristaDto payload, MotoristaDto existingDriver) {
        if (payload.getName() != null) {
            existingDriver.setName(payload.getName());
        }
        if (payload.getCnh() != null) {
            existingDriver.setCnh(payload.getCnh());
        }
        if (payload.getBirthDate() != null) {
            existingDriver.setBirthDate(payload.getBirthDate());
        }
        if (payload.getGender() != null) {
            existingDriver.setGender(payload.getGender());
        }
    }

    private MotoristaEntity getDriver(Long id) {
        return driverRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Driver with ID {} not found.", id);
            return new DriverNotFoundException(id);
        });
    }
}
