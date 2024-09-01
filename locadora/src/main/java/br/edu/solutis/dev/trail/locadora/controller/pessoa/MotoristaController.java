package br.edu.solutis.dev.trail.locadora.controller.pessoa;

import br.com.solutis.locadora.exception.person.driver.DriverException;
import br.com.solutis.locadora.exception.person.driver.DriverNotFoundException;
import br.com.solutis.locadora.model.dto.person.DriverDto;
import br.com.solutis.locadora.response.ErrorResponse;
import br.com.solutis.locadora.service.person.DriverService;
import br.com.solutis.locadora.service.rent.CartService;
import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.MotoristaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DriverController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
@CrossOrigin
public class MotoristaController {
    private final MotoristaService driverService;
    private final CartService cartService;

    @Operation(
            summary = "Listar por id",
            description = "Retorna as informações do motorista por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(driverService.findById(id), HttpStatus.OK);
        } catch (DriverNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (DriverException e) {
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
            return new ResponseEntity<>(driverService.findAll(page, size), HttpStatus.OK);
        } catch (DriverException e) {
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
            MotoristaDTO driverDto = driverService.add(payload);

            cartService.addByDriverId(driverDto.getId());

            return new ResponseEntity<>(driverDto, HttpStatus.CREATED);
        } catch (DriverException e) {
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
            return new ResponseEntity<>(driverService.update(payload), HttpStatus.NO_CONTENT);
        } catch (DriverNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (DriverException e) {
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
            cartService.deleteByDriverId(id);

            driverService.deleteById(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DriverNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (DriverException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}