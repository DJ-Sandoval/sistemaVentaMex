package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.presentation.api.POSAPI;
import org.springframework.stereotype.Controller;

@Controller
public class SisyemController implements POSAPI
{
    @Override
    public String mostrarPagina() {
        return "nueva-venta"; // se dirige al archivo templates/index.html
    }

    @Override
    public String mostrarLogin() {
        return "login"; // se dirige al archivo templates/index.html
    }

    @Override
    public String mostrarHome() {
        return "home";
    }

    @Override
    public String mostrarIndex() {
        return "home";
    }

    @Override
    public String mostrarFormRegistro() {
        return "registroUsuario";
    }








}
