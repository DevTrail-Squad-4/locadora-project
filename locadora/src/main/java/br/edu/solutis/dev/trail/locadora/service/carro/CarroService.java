package br.edu.solutis.dev.trail.locadora.service.carro;

import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.exception.carro.CarroException;
import br.edu.solutis.dev.trail.locadora.exception.carro.CarroNotFoundException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Fabricante.FabricanteNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.CarroDto;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.CarroDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Acessorio;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Carro;
import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
import br.edu.solutis.dev.trail.locadora.repository.carro.AcessorioRepository;
import br.edu.solutis.dev.trail.locadora.repository.carro.CarroRepository;
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
public class CarroService  {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarroService.class);
    private final CarroRepository carRepository;
    private final AcessorioRepository accessoryRepository;
    private final GenericMapper<CarroDto, Carro> modelMapper;
    private final GenericMapper<CarroDtoResponse, Carro> modelMapperResponse;

    public CarroDtoResponse findById(Long id) {
        LOGGER.info("Encontrar carro com ID {}", id);
        Carro car = getCar(id);

        return modelMapperResponse.mapModelToDto(car, CarroDtoResponse.class);
    }

    public PageResponse<CarroDtoResponse> findAll(int pageNo, int pageSize) {
        try {
            LOGGER.info("Buscando carros com número de página {} e tamanho de página {}.", pageNo, pageSize);

            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<Carro> pagedCars = carRepository.findByDeletedFalseAndAlugadoFalse(paging);
            List<CarroDtoResponse> carDtos = modelMapperResponse.mapList(pagedCars.getContent(),CarroDtoResponse.class);

            PageResponse<CarroDtoResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(carDtos);
            pageResponse.setCurrentPage(pagedCars.getNumber());
            pageResponse.setTotalItems(pagedCars.getTotalElements());
            pageResponse.setTotalPages(pagedCars.getTotalPages());

            return pageResponse;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new CarroException("Ocorreu um erro ao buscar carros.", e);
        }
    }

    public CarroDto add(CarroDto payload) {
        try {
            LOGGER.info("Adicionando carro {}", payload);

            List<Acessorio> accessories = accessoryRepository.findAllById(payload.getAccessoriesIds());

            Carro car = modelMapper.mapDtoToModel(payload, Carro.class);
            car.setAcessorios(accessories);
            car.setAlugado(false);

            return modelMapper.mapModelToDto(carRepository.save(car), CarroDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new CarroException("Ocorreu um erro ao adicionar o carro.", e);
        }
    }

    public CarroDtoResponse update(CarroDto payload) {
        Carro existingCar = getCar(payload.getId());
        if (existingCar.isDeleted()) throw new FabricanteNotFoundException(existingCar.getId());

        try {
            LOGGER.info("Atualizando carro {}", payload);
            CarroDto carDto = modelMapper
                    .mapModelToDto(existingCar, CarroDto.class);

            updateModelFields(payload, carDto);

            Carro car = carRepository
                    .save(modelMapper.mapDtoToModel(carDto, Carro.class));

            return modelMapperResponse.mapModelToDto(car, CarroDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new CarroException("Ocorreu um erro ao atualizar o carro.", e);
        }
    }

    public void deleteById(Long id) {


        try {
            LOGGER.info("Exclusão reversível de carro com ID {}", id);

            Carro car = getCar(id);
            car.setDeleted(true);

            carRepository.save(car);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new CarroException("Ocorreu um erro ao excluir o carro.", e);
        }
    }

    private void updateModelFields(CarroDto payload, CarroDto existingCar) {
        if (payload.getPlate() != null) {
            existingCar.setPlate(payload.getPlate());
        }
        if (payload.getChassis() != null) {
            existingCar.setChassis(payload.getChassis());
        }
        if (payload.getColor() != null) {
            existingCar.setColor(payload.getColor());
        }
        if (payload.getDailyValue() != null) {
            existingCar.setDailyValue(payload.getDailyValue());
        }
        if (payload.getImageUrl() != null) {
            existingCar.setImageUrl(payload.getImageUrl());
        }

    }

    public List<CarroDtoResponse> findCarsByFilters(
            ModeloCategoriaEnum category,
            Acessorio accessory,
            String model,
            Boolean rented) {

        List<Carro> cars = carRepository.findCarrosByFilters(category, accessory, model, rented);
        return modelMapperResponse.mapList(cars, CarroDtoResponse.class);
    }
    private Carro getCar(Long id) {
        return carRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Carro com ID {} não encontrado.", id);
            return new CarroNotFoundException(id);
        });
    }

}