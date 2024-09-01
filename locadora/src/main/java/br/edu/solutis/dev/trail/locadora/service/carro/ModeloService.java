package br.edu.solutis.dev.trail.locadora.service.carro;

import br.edu.solutis.dev.trail.locadora.exception.carro.Modelo.ModeloException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Modelo.ModeloNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.ModeloDto;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.ModeloDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Modelo;
import br.edu.solutis.dev.trail.locadora.repository.carro.ModeloRepository;
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
        LOGGER.info("Encontrando modelo com ID {}", id);
        Modelo model = getModel(id);

        return modelMapperResponse.mapModelToDto(model, ModeloDtoResponse.class);
    }

    public PageResponse<ModeloDtoResponse> findAll(int pageNo, int pageSize) {
        try {
            LOGGER.info("Buscando modelos com número de página {} e tamanho de página {}.", pageNo, pageSize);

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
            throw new ModeloException("Ocorreu um erro ao buscar modelos.", e);
        }
    }

    public ModeloDto add(ModeloDto payload) {
        try {
            LOGGER.info("Adding model: {}", payload);

            Modelo model = modelRepository.save(modelMapper.mapDtoToModel(payload, Modelo.class));

            return modelMapper.mapModelToDto(model, ModeloDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ModeloException("Ocorreu um erro ao adicionar o modelo do carro", e);
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
            throw new ModeloException("Ocorreu um erro ao atualizar o modelo do carro.", e);
        }
    }

    public void deleteById(Long id) {
        Modelo model = getModel(id);
        try {
            LOGGER.info("Modelo de exclusão reversível com ID {}", id);


            model.setDeleted(true);

            modelRepository.save(model);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ModeloException("Ocorreu um erro ao excluir o modelo do carro", e);
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
            LOGGER.error("Modelo com ID {} não encontrado.", id);
            return new ModeloNotFoundException(id);
        });
    }
}