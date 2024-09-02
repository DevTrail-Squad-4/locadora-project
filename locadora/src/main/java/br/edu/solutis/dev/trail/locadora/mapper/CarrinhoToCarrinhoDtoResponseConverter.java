package br.edu.solutis.dev.trail.locadora.mapper;

import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Carrinho;
import org.modelmapper.AbstractConverter;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.CarrinhoDtoResponse;
public class CarrinhoToCarrinhoDtoResponseConverter extends AbstractConverter<Carrinho, CarrinhoDtoResponse> {
    @Override
    protected CarrinhoDtoResponse convert(Carrinho carrinho) {
        CarrinhoDtoResponse carrinhoDtoResponse = new CarrinhoDtoResponse();

        carrinhoDtoResponse.setId(carrinho.getId());
        carrinhoDtoResponse.setNome(carrinho.getMotorista().getNome());
        carrinhoDtoResponse.setCpf(carrinho.getMotorista().getCpf());
        carrinhoDtoResponse.setAlugueis(carrinho.getAluguel());

        return carrinhoDtoResponse;
    }
}