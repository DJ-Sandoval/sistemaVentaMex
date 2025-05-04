package com.VentaMex.apiVentaMex.presentation.api;

import com.VentaMex.apiVentaMex.persistence.entities.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(UserAPI.BASE_URL)
public interface UserAPI {
    String BASE_URL="/api/usuarios";

    @GetMapping
    ResponseEntity<List<UserEntity>> getAllUsers();

    @GetMapping("/{id}")
    ResponseEntity<UserEntity> getUserById(@PathVariable Long id);
}
