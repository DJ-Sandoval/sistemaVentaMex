package com.VentaMex.apiVentaMex.presentation.api;

import com.VentaMex.apiVentaMex.persistence.entities.UserEntity;
import com.VentaMex.apiVentaMex.presentation.dto.AuthCreateUserRequest;
import com.VentaMex.apiVentaMex.presentation.dto.AuthLoginRequest;
import com.VentaMex.apiVentaMex.presentation.dto.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Validated
@RequestMapping(AuthAPI.BASE_URL)
public interface AuthAPI {
    String BASE_URL="/auth";

    @PostMapping("/sign-up")
    ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest);

    @PostMapping("/log-in")
    ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest,
                                       HttpServletResponse response);
}
