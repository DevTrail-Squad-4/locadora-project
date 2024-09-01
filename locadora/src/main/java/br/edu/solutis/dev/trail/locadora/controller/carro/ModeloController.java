package br.edu.solutis.dev.trail.locadora.controller.carro;

import br.edu.solutis.dev.trail.locadora.exception.carro.Modelo.ModeloException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Modelo.ModeloNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.ModeloDto;
import br.edu.solutis.dev.trail.locadora.service.carro.ModeloService;
import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ModeloController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/modelos")
@CrossOrigin
public class ModeloController {
    private final ModeloService modelService;


    @Operation(
            summary = "Listar por id",
            description = "Retorna as informações do modelo do carro por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(modelService.findById(id), HttpStatus.OK);
        } catch (ModeloNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ModeloException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listar todos os modelos de carros",
            description = "Retorna as informações de todos os modelos de carros"
    )
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            return new ResponseEntity<>(modelService.findAll(page, size), HttpStatus.OK);
        } catch (ModeloException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo modelo de carro",
            description = "Retorna as informações do novo modelo de carro adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody ModeloDto payload) {
        try {
            return new ResponseEntity<>(modelService.add(payload), HttpStatus.CREATED);
        } catch (ModeloException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualiza um modelo de carro",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ModeloDto payload) {
        try {
            return new ResponseEntity<>(modelService.update(payload), HttpStatus.NO_CONTENT);
        } catch (ModeloNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ModeloException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apaga um modelo por id",
            description = "Retorna o codigo 204 (No Content)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            modelService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ModeloNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ModeloException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}