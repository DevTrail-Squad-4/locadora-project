package br.edu.solutis.dev.trail.locadora.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GenericMapper<S, T> {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(new CarroToCarroDtoResponseConverter());
        modelMapper.addConverter(new ModeloToModeloDtoResponse());
        //modelMapper.addConverter(new CartToCartDtoResponseConverter());
        //modelMapper.addConverter(new RentToRentDtoResponseConverter());
        return modelMapper;
    }

    public T mapDtoToModel(S dto, Class<T> targetClass) {
        return this.modelMapper().map(dto, targetClass);
    }

    public S mapModelToDto(T model, Class<S> targetClass) {
        return this.modelMapper().map(model, targetClass);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> this.modelMapper().map(element, targetClass))
                .collect(Collectors.toList());
    }
}