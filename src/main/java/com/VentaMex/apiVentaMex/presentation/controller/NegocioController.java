package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.persistence.entities.NegocioEntity;
import com.VentaMex.apiVentaMex.presentation.api.BussinesAPI;
import com.VentaMex.apiVentaMex.service.interfaces.INegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/business")
@RequiredArgsConstructor
public class NegocioController {

    private final INegocioService negocioService;

    @GetMapping("/configTickets")
    public String mostrarPaginaConfigTickets(Model model) {
        model.addAttribute("negocios", negocioService.obtenerTodosNegocios());
        model.addAttribute("negocio", new NegocioEntity());
        return "ticketConfig";
    }

    @PostMapping("/negocio")
    public String crearNegocio(@ModelAttribute NegocioEntity negocio) {
        negocioService.crearNegocio(negocio);
        return "redirect:/web/configTickets";
    }

    @PostMapping("/negocio/{id}")
    public String actualizarNegocio(@PathVariable Long id, @ModelAttribute NegocioEntity negocio) {
        negocioService.actualizarNegocio(id, negocio);
        return "redirect:/web/configTickets";
    }

    @GetMapping("/negocio/{id}")
    @ResponseBody
    public NegocioEntity obtenerNegocio(@PathVariable Long id) {
        return negocioService.obtenerNegocioPorId(id)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
    }
}