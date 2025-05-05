package com.VentaMex.apiVentaMex.service.imp;

import com.VentaMex.apiVentaMex.persistence.entities.NegocioEntity;
import com.VentaMex.apiVentaMex.persistence.repository.NegocioRepository;
import com.VentaMex.apiVentaMex.service.interfaces.INegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NegocioServiceImp implements INegocioService {
    private final NegocioRepository negocioRepository;

    @Override
    public NegocioEntity crearNegocio(NegocioEntity negocio) {
        return negocioRepository.save(negocio);
    }

    @Override
    public NegocioEntity actualizarNegocio(Long id, NegocioEntity negocio) {
        NegocioEntity existente = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado con ID: " + id));
        existente.setNombreNegocio(negocio.getNombreNegocio());
        existente.setNombrePropietario(negocio.getNombrePropietario());
        existente.setRfc(negocio.getRfc());
        existente.setDomicilio(negocio.getDomicilio());
        existente.setTelefono(negocio.getTelefono());
        existente.setImagen(negocio.getImagen());
        return negocioRepository.save(existente);
    }

    @Override
    public Optional<NegocioEntity> obtenerNegocioPorId(Long id) {
        return negocioRepository.findById(id);
    }

    @Override
    public List<NegocioEntity> obtenerTodosNegocios() {
        return negocioRepository.findAll();
    }
}
