package br.edu.solutis.dev.trail.locadora.mapper;

import br.edu.solutis.dev.trail.locadora.model.dto.carro.CarroDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Carro;

import org.modelmapper.AbstractConverter;



public class CarroParaCarroResponseDto extends AbstractConverter<Carro, CarroDtoResponse> {
    @Override
    protected CarroDtoResponse convert(Carro car) {
        CarroDtoResponse CarroDtoResponse = new CarroDtoResponse();

        CarroDtoResponse.setId(car.getId());
        CarroDtoResponse.setColor(car.getColor());
        CarroDtoResponse.setPlate(car.getPlate());
        CarroDtoResponse.setChassis(car.getChassis());
        CarroDtoResponse.setDailyValue(car.getDailyValue());
        CarroDtoResponse.setRented(car.isRented());

        CarroDtoResponse.setAccessories(car.getAccessories());

        if (car.getModel() != null) {
            CarroDtoResponse.setDescription(car.getModel().getDescription());
            CarroDtoResponse.setCategory(car.getModel().getCategory());
            if (car.getModel().getManufacturer() != null) {
                CarroDtoResponse.setName(car.getModel().getManufacturer().getName());
            }
        }

        return CarroDtoResponse;
    }

}