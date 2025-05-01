package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.presentation.api.CategoryAPI;
import com.VentaMex.apiVentaMex.presentation.dto.CategoriaDTO;
import com.VentaMex.apiVentaMex.service.interfaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoriaController implements CategoryAPI {

    @Autowired
    private ICategoriaService categoriaService;

    @Override
    public ResponseEntity<Page<CategoriaDTO>> obtenerTodasCategorias(Pageable pageable) {
        Page<CategoriaDTO> categorias = categoriaService.obtenerTodasCategorias(pageable);
        return ResponseEntity.ok(categorias);
    }

    @Override
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(Long id) {
        CategoriaDTO categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @Override
    public ResponseEntity<CategoriaDTO> crearCategoria(CategoriaDTO categoriaDTO) {
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(categoriaDTO);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CategoriaDTO> actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        CategoriaDTO actualizada = categoriaService.actualizarCategoria(id, categoriaDTO);
        return ResponseEntity.ok(actualizada);
    }

    @Override
    public ResponseEntity<Void> eliminarCategoria(Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
