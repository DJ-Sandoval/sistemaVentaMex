package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.presentation.api.POSAPI;
import com.VentaMex.apiVentaMex.service.interfaces.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class SisyemController implements POSAPI {
    @Autowired
    private IProductoService productoService;
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

    @Override
    public String mostrarPaginaProductos() {
        return "productos";
    }

    @Override
    public String mostrarPaginaClientes() {
        return "clientes";
    }

    @Override
    public String mostrarPaginaCategorias() {
        return "categorias";
    }

    @Override
    public String mostrarPaginaMedidas() {
        return "medidas";
    }

    @Override
    public String mostrarPaginaBackups() {
        return "backups";
    }


}
