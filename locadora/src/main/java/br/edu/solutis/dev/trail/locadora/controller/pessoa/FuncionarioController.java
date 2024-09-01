package br.edu.solutis.dev.trail.locadora.controller.pessoa;


import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;
import br.edu.solutis.dev.trail.locadora.service.pessoa.FuncionarioService;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.funcionario.FuncionarioException;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.funcionario.FuncionarioNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.FuncionarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FuncionarioController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/funcionarios")
@CrossOrigin
public class FuncionarioController {
    private final FuncionarioService funcionarioService;

    @Operation(
            summary = "Listar por id",
            description = "Retorna as informações do funcionario por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(funcionarioService.findById(id), HttpStatus.OK);
        } catch (FuncionarioNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FuncionarioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listar todos",
            description = "Retorna as informações de todos os funcionarios"
    )
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        try {
            return new ResponseEntity<>(funcionarioService.findAll(page, size), HttpStatus.OK);
        } catch (FuncionarioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo funcionario",
            description = "Retorna as informações do funcionario adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody FuncionarioDTO payload) {
        try {
            FuncionarioDTO funcionarioDto = funcionarioService.add(payload);

            return new ResponseEntity<>(funcionarioDto, HttpStatus.CREATED);
        } catch (FuncionarioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualiza um funcionario",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody FuncionarioDTO payload) {
        try {
            return new ResponseEntity<>(funcionarioService.update(payload), HttpStatus.NO_CONTENT);
        } catch (FuncionarioNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FuncionarioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apaga um funcionario por id",
            description = "Retorna o codigo 204 (No Content)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            funcionarioService.deleteById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FuncionarioNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FuncionarioException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}