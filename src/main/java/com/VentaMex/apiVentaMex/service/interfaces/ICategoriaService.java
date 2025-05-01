package com.VentaMex.apiVentaMex.service.interfaces;

import com.VentaMex.apiVentaMex.persistence.entities.Categoria;
import com.VentaMex.apiVentaMex.presentation.dto.CategoriaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoriaService {
    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO);
    CategoriaDTO obtenerCategoriaPorId(Long id);
    Page<CategoriaDTO> obtenerTodasCategorias(Pageable pageable);
    CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO);
    void eliminarCategoria(Long id);

}
