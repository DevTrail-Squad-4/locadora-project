package br.edu.solutis.dev.trail.locadora.controller.carro;

import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;
import br.edu.solutis.dev.trail.locadora.exception.carro.CarroException;
import br.edu.solutis.dev.trail.locadora.exception.carro.CarroNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.CarroDto;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.CarroDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Acessorio;
import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
import br.edu.solutis.dev.trail.locadora.service.carro.CarroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "CarroController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/carros")
@CrossOrigin
public class CarroController {
    private final CarroService carService;

    @Operation(
            summary = "Listar por id",
            description = "Retorna as informações do carro por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(carService.findById(id), HttpStatus.OK);
        } catch (CarroNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CarroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listar todos",
            description = "Retorna as informações de todos os carros"
    )
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            return new ResponseEntity<>(carService.findAll(page, size), HttpStatus.OK);
        } catch (CarroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo carro",
            description = "Retorna as informações do carro adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody CarroDto payload) {
        try {
            return new ResponseEntity<>(carService.add(payload), HttpStatus.CREATED);
        } catch (CarroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualiza um carro",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody CarroDto payload) {
        try {
            return new ResponseEntity<>(carService.update(payload), HttpStatus.NO_CONTENT);
        } catch (CarroNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CarroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apaga um carro por id",
            description = "Retorna o codigo 204 (No Content)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            carService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CarroNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CarroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "lista carros por filtro",
            description = "Retorna os carros de acordo com o filtro"
    )
    @GetMapping("/filtered")
    public ResponseEntity<List<CarroDtoResponse>> findCarsByFilters(
            @RequestParam(value = "category", required = false) ModeloCategoriaEnum category,
            @RequestParam(value = "accessory", required = false) Acessorio accessory,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "rented") Boolean rented) {
        List<CarroDtoResponse> cars = carService.findCarsByFilters(category, accessory, model, rented);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
}