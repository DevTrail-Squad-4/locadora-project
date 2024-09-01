package br.edu.solutis.dev.trail.locadora.controller.aluguel;

import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelNotFoundException;
import br.edu.solutis.dev.trail.locadora.service.aluguel.AluguelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;

@Tag(name = "AluguelController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/alugueis")
@CrossOrigin
public class AluguelController {
    private final AluguelService aluguelService;

    @Operation(
            summary = "Finalizando o aluguel - Devolução do carro",
            description = "Retorna as informações do aluguel"
    )
    @PostMapping("/{motoristaId}/alugueis/{alugueisId}/final")
    public ResponseEntity<?> finishAluguel(
            @PathVariable Long motoristaId,
            @PathVariable Long aluguelId
    ) {
        try {
            return new ResponseEntity<>(aluguelService.finishAluguel(motoristaId, aluguelId), HttpStatus.OK);
        } catch (AluguelNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (AluguelException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(
            summary = "Listando o alugueis finalizados",
            description = "Retorna as informações dos alugueis finalizados"
    )
    @GetMapping("/finalizado")
    public ResponseEntity<?> finishRent() {
        try {
            return new ResponseEntity<>(aluguelService.findAlugueisFinalizados(), HttpStatus.OK);
        } catch (AluguelException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Listando o alugueis ativos",
            description = "Retorna as informações dos alugueis ativos"
    )
    @GetMapping("/ativo")
    public ResponseEntity<?> findActiveRents() {
        try {
            return new ResponseEntity<>(aluguelService.findAlugueisAtivos(), HttpStatus.OK);
        } catch (AluguelException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
