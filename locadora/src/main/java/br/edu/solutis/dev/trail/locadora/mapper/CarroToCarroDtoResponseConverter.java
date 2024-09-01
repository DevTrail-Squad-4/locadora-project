package br.edu.solutis.dev.trail.locadora.mapper;

import br.edu.solutis.dev.trail.locadora.model.dto.carro.CarroDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Carro;

import org.modelmapper.AbstractConverter;



public class CarroToCarroDtoResponseConverter extends AbstractConverter<Carro, CarroDtoResponse> {
    @Override
    protected CarroDtoResponse convert(Carro car) {
        CarroDtoResponse CarroDtoResponse = new CarroDtoResponse();

        CarroDtoResponse.setId(car.getId());
        CarroDtoResponse.setColor(car.getColor());
        CarroDtoResponse.setPlate(car.getPlate());
        CarroDtoResponse.setChassis(car.getChassis());
        CarroDtoResponse.setDailyValue(car.getValorDiario());
        CarroDtoResponse.setRented(car.isAlugado());

        CarroDtoResponse.setAccessories(car.getAcessorios());

        if (car.getModelo() != null) {
            CarroDtoResponse.setDescription(car.getModelo().getDescricao());
            CarroDtoResponse.setCategory(car.getModelo().getCategoria());
            if (car.getModelo() .getFabricante() != null) {
                CarroDtoResponse.setName(car.getModelo() .getFabricante().getName());
            }
        }

        return CarroDtoResponse;
    }

}