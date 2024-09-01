package br.edu.solutis.dev.trail.locadora.mapper;

import br.edu.solutis.dev.trail.locadora.model.dto.carro.ModeloDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Modelo;
import org.modelmapper.AbstractConverter;

public class ModeloToModeloDtoResponse extends AbstractConverter<Modelo, ModeloDtoResponse> {
    @Override
    protected ModeloDtoResponse convert(Modelo modelo) {
        ModeloDtoResponse modeloDtoResponse = new ModeloDtoResponse();

        modeloDtoResponse.setId(modelo.getId());
        modeloDtoResponse.setCategory(modelo.getCategoria());
        modeloDtoResponse.setModel(modelo.getDescricao());
        modeloDtoResponse.setManufacturer(modelo.getFabricante().getName());

        return modeloDtoResponse;
    }
}
