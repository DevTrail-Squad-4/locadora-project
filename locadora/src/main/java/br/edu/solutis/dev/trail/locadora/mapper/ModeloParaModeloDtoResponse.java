package br.edu.solutis.dev.trail.locadora.mapper;

import br.edu.solutis.dev.trail.locadora.model.dto.carro.ModeloDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Modelo;
import org.modelmapper.AbstractConverter;

public class ModeloParaModeloDtoResponse extends AbstractConverter<Modelo, ModeloDtoResponse> {
    @Override
    protected ModeloDtoResponse convert(Modelo modelo) {
        ModeloDtoResponse modeloDtoResponse = new ModeloDtoResponse();

        modeloDtoResponse.setId(modelo.getId());
        modeloDtoResponse.setCategory(modelo.getCategory());
        modeloDtoResponse.setModel(modelo.getDescription());
        //modeloDtoResponse.setFabricante(modelo.getFabricante().getNome());

        return modeloDtoResponse;
    }
}
