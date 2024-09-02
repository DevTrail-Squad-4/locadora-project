package br.edu.solutis.dev.trail.locadora.controller.carro;

import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;
import br.edu.solutis.dev.trail.locadora.exception.carro.Acessorio.AcessorioException;
import br.edu.solutis.dev.trail.locadora.exception.carro.Acessorio.AcessorioNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.carro.AcessorioDto;
import br.edu.solutis.dev.trail.locadora.service.carro.AcessorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AccessorioController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/acessorios")
@CrossOrigin
public class AcessorioController {
    private final AcessorioService acessorioService;

    @Operation(
            summary = "Listar por id",
            description = "Retorna as informações do acessório por id")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(acessorioService.findById(id), HttpStatus.OK);
        } catch (AcessorioNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (AcessorioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listar todos",
            description = "Retorna as informações de todos os acessórios"
    )
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            return new ResponseEntity<>(acessorioService.findAll(page, size), HttpStatus.OK);
        } catch (AcessorioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo acessório",
            description = "Retorna as informações do acessório adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody AcessorioDto payload) {
        try {
            return new ResponseEntity<>(acessorioService.add(payload), HttpStatus.CREATED);
        } catch (AcessorioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualizar um acessório",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody AcessorioDto payload) {
        try {
            return new ResponseEntity<>(acessorioService.update(payload), HttpStatus.NO_CONTENT);
        } catch (AcessorioNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (AcessorioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apagar um acessório por id",
            description = "Retorna o codigo 204 (No Content)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            acessorioService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (AcessorioNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (AcessorioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}