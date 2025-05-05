package com.VentaMex.apiVentaMex.presentation.api;

import com.VentaMex.apiVentaMex.persistence.entities.NegocioEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping(BussinesAPI.BASE_URL)
public interface BussinesAPI {
    String BASE_URL="/negocio";

    @GetMapping
    String mostrarConfiguracion(Model model);

    @PostMapping
    String crearNegocio(@ModelAttribute NegocioEntity negocio);

    @PostMapping("/{id}")
    String actualizarNegocio(@PathVariable Long id, @ModelAttribute NegocioEntity negocio);

    @GetMapping("/{id}")
    @ResponseBody
    NegocioEntity obtenerNegocio(@PathVariable Long id);
}
