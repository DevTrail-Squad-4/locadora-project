package br.com.solutis.locadora.controller.carro;

import br.com.solutis.locadora.exception.car.manufacturer.ManufacturerException;
import br.com.solutis.locadora.exception.car.manufacturer.ManufacturerNotFoundException;
import br.com.solutis.locadora.model.dto.car.ManufacturerDto;
import br.com.solutis.locadora.response.ErrorResponse;
import br.com.solutis.locadora.service.car.ManufacturerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Controle Fabricante")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/fabricante")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    @Operation(
            summary = "Listar os fabricantes por id",
            description = "Retorna as informações do fabricante por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(manufacturerService.findById(id), HttpStatus.OK);
        } catch (ManufacturerNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ManufacturerException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listar todos os fabricantes",
            description = "Retorna as informações de todos os fabricantes"
    )
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        try {
            return new ResponseEntity<>(manufacturerService.findAll(page, size), HttpStatus.OK);
        } catch (ManufacturerException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo fabricante",
            description = "Retorna as informações do fabricante adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody ManufacturerDto payload) {
        try {
            return new ResponseEntity<>(manufacturerService.add(payload), HttpStatus.CREATED);
        } catch (ManufacturerException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualiza um fabricante",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ManufacturerDto payload) {
        try {
            return new ResponseEntity<>(manufacturerService.update(payload), HttpStatus.NO_CONTENT);
        } catch (ManufacturerNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ManufacturerException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apaga um fabricante por id",
            description = "Retorna o codigo 204(No Content)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            manufacturerService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ManufacturerNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ManufacturerException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}