package br.edu.solutis.dev.trail.locadora.controller.aluguel;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "ApoliceSeguroController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/seguros")
@CrossOrigin
public class ApoliceSeguroController {
    private final ApoliceSeguroService apoliceSeguroService;

    @Operation(
            summary = "Listar por id",
            description = "Retorna as informações do seguro por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(ApoliceSeguroService.findById(id), HttpStatus.OK);
        } catch (ApoliceSeguroNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ApoliceSeguroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listar todos",
            description = "Retorna as informações de todos os seguros"
    )
    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "3") int tamanho
    ) {
        try {
            return new ResponseEntity<>(ApoliceSeguroService.findAll(pagina, tamanho), HttpStatus.OK);
        } catch (ApoliceSeguroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Adicionar um novo seguro",
            description = "Retorna as informações do seguro adicionado"
    )
    @PostMapping
    public ResponseEntity<?> add(@RequestBody ApoliceSeguroDto payload) {
        try {
            return new ResponseEntity<>(ApoliceSeguroService.add(payload), HttpStatus.CREATED);
        } catch (ApoliceSeguroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Atualiza um seguro",
            description = "Retorna o codigo 204 (No Content)"
    )
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ApoliceSeguroDto payload) {
        try {
            return new ResponseEntity<>(ApoliceSeguroService.update(payload), HttpStatus.NO_CONTENT);
        } catch (ApoliceSeguroNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ApoliceSeguroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apaga um seguro por id",
            description = "Retorna o codigo 204 (No Content)"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            ApoliceSeguroService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ApoliceSeguroNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ApoliceSeguroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
