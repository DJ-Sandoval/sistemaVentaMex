package com.VentaMex.apiVentaMex.configuration.app;

import com.VentaMex.apiVentaMex.persistence.entities.Categoria;
import com.VentaMex.apiVentaMex.persistence.entities.Medida;
import com.VentaMex.apiVentaMex.presentation.dto.CategoriaDTO;
import com.VentaMex.apiVentaMex.presentation.dto.MedidaDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Ignorar la lista de productos en CategoriaDTO y MedidaDTO
        modelMapper.typeMap(Categoria.class, CategoriaDTO.class)
                .addMappings(mapper -> mapper.skip(CategoriaDTO::setProductos));
        modelMapper.typeMap(Medida.class, MedidaDTO.class)
                .addMappings(mapper -> mapper.skip(MedidaDTO::setProductos));
        return modelMapper;
    }
}
