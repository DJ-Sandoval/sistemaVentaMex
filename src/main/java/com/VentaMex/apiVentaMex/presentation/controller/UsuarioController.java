package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.persistence.entities.UserEntity;
import com.VentaMex.apiVentaMex.persistence.repository.UserRepository;
import com.VentaMex.apiVentaMex.presentation.api.UserAPI;
import com.VentaMex.apiVentaMex.service.imp.UserDetailServiceImpl;
import com.VentaMex.apiVentaMex.service.imp.UsuarioServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsuarioController implements UserAPI{
    @Autowired
    private UsuarioServiceImp usuarioService;


    @Override
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = usuarioService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserEntity> getUserById(Long id) {
        UserEntity user = usuarioService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
