package br.edu.solutis.dev.trail.locadora.controller.carro;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.solutis.dev.trail.locadora.exception.carro.Fabricante.FabricanteException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Fabricante.FabricanteNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.FabricanteDto;
import br.edu.solutis.dev.trail.locadora.service.carro.FabricanteService;
import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;

@Tag(name = "FabricanteController")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/fabricante")
public class FabricanteController {
    private final FabricanteService manufacturerService;

    @Operation(
            summary = "Listar os fabricantes por id",
            description = "Retorna as informações do fabricante por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(manufacturerService.findById(id), HttpStatus.OK);
        } catch (FabricanteNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FabricanteException e) {
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
        } catch (FabricanteException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo fabricante",
            description = "Retorna as informações do fabricante adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody FabricanteDto payload) {
        try {
            return new ResponseEntity<>(manufacturerService.add(payload), HttpStatus.CREATED);
        } catch (FabricanteException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualiza um fabricante",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody FabricanteDto payload) {
        try {
            return new ResponseEntity<>(manufacturerService.update(payload), HttpStatus.NO_CONTENT);
        } catch (FabricanteNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FabricanteException e) {
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
        } catch (FabricanteNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FabricanteException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}