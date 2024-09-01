package br.edu.solutis.dev.trail.locadora.mapper;

import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.AluguelDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Aluguel;
import org.modelmapper.AbstractConverter;

public class AluguelParaAluguelDTOResponseConverter extends AbstractConverter<Aluguel, AluguelDtoResponse> {
    @Override
    protected AluguelDtoResponse convert(Aluguel aluguel) {

        AluguelDtoResponse aluguelDtoResponse = new AluguelDtoResponse();

        aluguelDtoResponse.setId(aluguel.getId());
        aluguelDtoResponse.setCpf(aluguel.getMotorista().getCpf());
        aluguelDtoResponse.setMotoristaNome(aluguel.getMotorista().getNome());
        aluguelDtoResponse.setDataInicial(aluguel.getDataInicial());
        aluguelDtoResponse.setDataFinal(aluguel.getDataFinal());
        aluguelDtoResponse.setDataTermino(aluguel.getDataFinalizada());
        aluguelDtoResponse.setValor(aluguel.getValor());
        aluguelDtoResponse.setConfirmado(aluguel.isConfirmado());
        aluguelDtoResponse.setFinalizado(aluguel.isFinalizado());
        aluguelDtoResponse.setFranquiaValor(aluguel.getApoliceSeguro().getValorFranquia());
        aluguelDtoResponse.setCoberturaTerceiros(aluguel.getApoliceSeguro().isProtecaoTerceiro());
        aluguelDtoResponse.setCoberturaFenomenos(aluguel.getApoliceSeguro().isProtecaoCausasNaturais());
        aluguelDtoResponse.setCoberturaRoubo(aluguel.getApoliceSeguro().isProtecaoRoubo());
        aluguelDtoResponse.setPlaca(aluguel.getCarro().getPlate());
        aluguelDtoResponse.setCor(aluguel.getCarro().getColor());
        if(aluguel.getCarro().getModelo()!= null){
            aluguelDtoResponse.setModelo(aluguel.getCarro().getModelo().getDescricao());
            aluguelDtoResponse.setCategoria(aluguel.getCarro().getModelo().getCategoria());
            aluguelDtoResponse.setFabricante(aluguel.getCarro().getModelo().getFabricante().getName());
        }

        return aluguelDtoResponse;
    }
}

