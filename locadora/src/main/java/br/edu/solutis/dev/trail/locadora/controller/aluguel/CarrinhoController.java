package br.edu.solutis.dev.trail.locadora.controller.aluguel;

import br.edu.solutis.dev.trail.locadora.exception.carro.CarroException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelNotFoundException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.carrinho.CarrinhoException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.carrinho.CarrinhoNotFoundException;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.CarrinhoDto;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.CarrinhoDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.AluguelDto;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.AluguelDtoResponse;
import br.edu.solutis.dev.trail.locadora.response.ErrorResponse;
import br.edu.solutis.dev.trail.locadora.service.aluguel.CarrinhoService;
import br.edu.solutis.dev.trail.locadora.service.aluguel.CarrinhoService;
import br.edu.solutis.dev.trail.locadora.service.aluguel.AluguelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "CarrinhoController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/carrinhos")
@CrossOrigin
public class CarrinhoController {
    private final CarrinhoService carrinhoService;
    private final AluguelService aluguelService;

    @Operation(
            summary = "Lista o carrinho de um motorista",
            description = "Retorna as informações do carrinho do motorista"
    )
    @GetMapping("/{motoristaId}")
    public ResponseEntity<?> findCarrinhoByMotoristaId(@PathVariable Long motoristaId) {
        try {
            return new ResponseEntity<>(carrinhoService.findByMotoristaId(motoristaId), HttpStatus.OK);
        } catch (CarrinhoNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CarrinhoException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Lista todos os carrinhos",
            description = "Retorna as informações de todos os carrinhos"
    )
    @GetMapping
    public ResponseEntity<?> findAllCarrinhos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "3") int tamanho
    ) {
        try {
            return new ResponseEntity<>(carrinhoService.findAll(pagina, tamanho), HttpStatus.OK);
        } catch (CarrinhoException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Criando o aluguel",
            description = "Retorna as informações do carrinho"
    )
    @PostMapping("/{motoristaId}/alugueis")
    public ResponseEntity<?> addAluguel(@PathVariable Long motoristaId, @RequestBody AluguelDto payload) {
        try {
            payload.setMotoristaId(motoristaId);
            AluguelDto aluguelDto = aluguelService.add(payload);

            carrinhoService.addAluguelToCarrinhoByMotoristaId(motoristaId, aluguelDto.getId());

            return new ResponseEntity<>(aluguelDto, HttpStatus.OK);
        } catch (CarroException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CarrinhoNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CarrinhoException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Confirma o aluguel do carrinho",
            description = "Retorna as informações do carrinho"
    )
    @PostMapping("/{motoristaId}/alugueis/{aluguelId}/confirm")
    public ResponseEntity<?> confirmAluguelFromCarrinho(@PathVariable Long motoristaId, @PathVariable Long aluguelId) {
        try {
            aluguelService.confirmAluguel(aluguelId);
            CarrinhoDtoResponse carrinhoDto = carrinhoService.removeAluguelFromCarrinhoByMotoristaId(motoristaId, aluguelId);

            return new ResponseEntity<>(carrinhoDto, HttpStatus.OK);
        } catch (AluguelNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CarroException | AluguelException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (CarrinhoException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Apaga o aluguel do carrinho",
            description = "Retorna o codigo 204 (No Content)"
    )
    @DeleteMapping("/{motoristaId}/alugueis/{aluguelId}")
    public ResponseEntity<?> deleteAluguelFromCarrinho(@PathVariable Long motoristaId, @PathVariable Long aluguelId) {
        try {
            carrinhoService.removeAluguelFromCarrinhoByMotoristaId(motoristaId, aluguelId);
            aluguelService.deleteById(aluguelId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CarrinhoNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (CarrinhoException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}