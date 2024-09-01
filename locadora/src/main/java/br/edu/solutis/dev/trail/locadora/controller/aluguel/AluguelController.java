package br.com.solutis.locadora.controller.rent;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RentController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/alugueis")
@CrossOrigin
public class ALuguelController {
    private final AluguelService aluguelService;

    @Operation(
            summary = "Finalizando o aluguel - Devolução do carro",
            description = "Retorna as informações do aluguel"
    )
    @PostMapping("/{motoristaId}/alugueis/{alugueisId}/final")
    public ResponseEntity<?> finishRent(
            @PathVariable Long motoristaId,
            @PathVariable Long aluguelId
    ) {
        try {
            return new ResponseEntity<>(aluguelService.finishRent(motoristaId, aluguelId), HttpStatus.OK);
        } catch (RentNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (RentException e) {
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
            return new ResponseEntity<>(aluguelService.findFinishedRents(), HttpStatus.OK);
        } catch (RentException e) {
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
            return new ResponseEntity<>(aluguelService.findActiveRents(), HttpStatus.OK);
        } catch (RentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
