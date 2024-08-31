package br.edu.solutis.dev.trail.locadora.service.carro;

import br.edu.solutis.dev.trail.locadora.exception.carro.Modelo.ModeloException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Modelo.ModeloNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.ModeloDto;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.ModeloDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Modelo;
import br.edu.solutis.dev.trail.locadora.repository.ModeloRepository;
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
public class ModeloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModeloService.class);
    private final ModeloRepository modelRepository;
    private final GenericMapper<ModeloDto, Modelo> modelMapper;
    private final GenericMapper<ModeloDtoResponse, Modelo> modelMapperResponse;

    public ModeloDtoResponse findById(Long id) {
        LOGGER.info("Finding model with ID: {}", id);
        Modelo model = getModel(id);

        return modelMapperResponse.mapModelToDto(model, ModeloDtoResponse.class);
    }

    public PageResponse<ModeloDtoResponse> findAll(int pageNo, int pageSize) {
        try {
            LOGGER.info("Fetching models with page number {} and page size {}.", pageNo, pageSize);

            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<Modelo> pagedModels = modelRepository.findByDeletedFalse(paging);

            List<ModeloDtoResponse> modelDtos = modelMapper
                    .mapList(pagedModels.getContent(), ModeloDtoResponse.class);

            PageResponse<ModeloDtoResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(modelDtos);
            pageResponse.setCurrentPage(pagedModels.getNumber());
            pageResponse.setTotalItems(pagedModels.getTotalElements());
            pageResponse.setTotalPages(pagedModels.getTotalPages());

            return pageResponse;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ModeloException("An error occurred while fetching models.", e);
        }
    }

    public ModeloDto add(ModeloDto payload) {
        try {
            LOGGER.info("Adding model: {}", payload);

            Modelo model = modelRepository.save(modelMapper.mapDtoToModel(payload, Modelo.class));

            return modelMapper.mapModelToDto(model, ModeloDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ModeloException("An error occurred while adding the car model", e);
        }
    }

    public ModeloDtoResponse update(ModeloDto payload) {
        Modelo existingModel = getModel(payload.getId());
        if (existingModel.isDeleted()) throw new ModeloNotFoundException(existingModel.getId());

        try {
            LOGGER.info("Updating model: {}", payload);
            ModeloDto modelDto = modelMapper
                    .mapModelToDto(existingModel, ModeloDto.class);

            updateModelFields(payload, modelDto);

            Modelo model = modelRepository
                    .save(modelMapper.mapDtoToModel(modelDto, Modelo.class));

            return modelMapperResponse.mapModelToDto(model, ModeloDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ModeloException("An error occurred while updating the car model.", e);
        }
    }

    public void deleteById(Long id) {
        Modelo model = getModel(id);
        try {
            LOGGER.info("Soft deleting model with ID {}", id);


            model.setDeleted(true);

            modelRepository.save(model);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ModeloException("An error occurred while deleting the car model", e);
        }
    }

    private void updateModelFields(ModeloDto payload, ModeloDto existingModel) {
        if (payload.getDescription() != null) {
            existingModel.setDescription(payload.getDescription());
        }
        if (payload.getCategory() != null) {
            existingModel.setCategory(payload.getCategory());
        }
        if (payload.getManufacturerId() != null) {
            existingModel.setManufacturerId(payload.getManufacturerId());
        }
    }

    private Modelo getModel(Long id) {
        return modelRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Model with ID {} not found.", id);
            return new ModeloNotFoundException(id);
        });
    }
}