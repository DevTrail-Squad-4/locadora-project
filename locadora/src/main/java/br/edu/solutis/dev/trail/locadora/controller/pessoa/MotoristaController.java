package br.edu.solutis.dev.trail.locadora.controller.pessoa;

import br.com.solutis.locadora.service.person.MotoristaService;
import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista.MotoristaException;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista.MotoristaNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.MotoristaDTO;
import br.edu.solutis.dev.trail.locadora.service.carro.CarroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MotoristaController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/motoristas")
@CrossOrigin
public class MotoristaController {
    private final MotoristaService motoristaService;
    private final CarrinhoService carrinhoService;

    @Operation(
            summary = "Listar por id",
            description = "Retorna as informações do motorista por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(motoristaService.findById(id), HttpStatus.OK);
        } catch (MotoristaNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (MotoristaException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listar todos",
            description = "Retorna as informações de todos os motoristas"
    )
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        try {
            return new ResponseEntity<>(motoristaService.findAll(page, size), HttpStatus.OK);
        } catch (MotoristaException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo motorista",
            description = "Retorna as informações do motorista adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody MotoristaDTO payload) {
        try {
            MotoristaDTO driverDto = MotoristaService.add(payload);

            carrinhoService.addByMotoristaId(driverDto.getId());

            return new ResponseEntity<>(driverDto, HttpStatus.CREATED);
        } catch (MotoristaException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualiza um motorista",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody MotoristaDTO payload) {
        try {
            return new ResponseEntity<>(motoristaService.update(payload), HttpStatus.NO_CONTENT);
        } catch (MotoristaNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (MotoristaException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apaga um motorista por id",
            description = "Retorna o codigo 204 (No Content)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            carrinhoService.deleteByMotoristaId(id);

            motoristaService.deleteById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (MotoristaNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (MotoristaException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}