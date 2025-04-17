package com.VentaMex.apiVentaMex.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class SisyemController {
    @GetMapping("/venta")
    public String mostrarPagina() {
        return "nueva-venta"; // se dirige al archivo templates/index.html
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // se dirige al archivo templates/index.html
    }

    @GetMapping("/home")
    public String mostrarHome() {
        return "home";
    }

    @GetMapping("/index")
    public String mostrarIndex() {
        return "home";
    }

    @GetMapping("/registro")
    public String mostrarFormRegistro() {
        return "registroUsuario";
    }








}
