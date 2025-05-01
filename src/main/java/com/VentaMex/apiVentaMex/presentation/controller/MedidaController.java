package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.presentation.api.ExtentAPI;
import com.VentaMex.apiVentaMex.presentation.dto.MedidaDTO;
import com.VentaMex.apiVentaMex.service.interfaces.IMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MedidaController implements ExtentAPI {
    @Autowired
    private IMedidaService medidaService;

    @Override
    public ResponseEntity<Page<MedidaDTO>> obtenerTodasMedidas(Pageable pageable) {
        Page<MedidaDTO> medidas = medidaService.obtenerTodasMedidas(pageable);
        return ResponseEntity.ok(medidas);
    }

    @Override
    public ResponseEntity<MedidaDTO> obtenerMedidaPorId(Long id) {
        MedidaDTO medida = medidaService.obtenerMedidaPorId(id);
        return ResponseEntity.ok(medida);
    }

    @Override
    public ResponseEntity<MedidaDTO> crearMedida(MedidaDTO medidaDTO) {
        MedidaDTO nuevaMedida = medidaService.crearMedida(medidaDTO);
        return new ResponseEntity<>(nuevaMedida, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MedidaDTO> actualizarMedida(Long id, MedidaDTO medidaDTO) {
        MedidaDTO actualizada = medidaService.actualizarMedida(id, medidaDTO);
        return ResponseEntity.ok(actualizada);
    }

    @Override
    public ResponseEntity<Void> eliminarMedida(Long id) {
        medidaService.eliminarMedida(id);
        return ResponseEntity.noContent().build();
    }
}
