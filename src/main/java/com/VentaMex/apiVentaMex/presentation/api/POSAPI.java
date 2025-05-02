package com.VentaMex.apiVentaMex.presentation.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping(POSAPI.BASE_URL)
public interface POSAPI {
    String BASE_URL="/web";

    @GetMapping("/venta")
    String mostrarPagina();

    @GetMapping("/login")
    String mostrarLogin();

    @GetMapping("/home")
    String mostrarHome();

    @GetMapping("/index")
    String mostrarIndex();

    @GetMapping("/registro")
    String mostrarFormRegistro();

    @GetMapping("/productos")
    String mostrarPaginaProductos();
}
