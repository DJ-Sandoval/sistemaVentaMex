package com.VentaMex.apiVentaMex.presentation.controller;

import com.VentaMex.apiVentaMex.presentation.dto.AuthCreateUserRequest;
import com.VentaMex.apiVentaMex.presentation.dto.AuthLoginRequest;
import com.VentaMex.apiVentaMex.presentation.dto.AuthResponse;
import com.VentaMex.apiVentaMex.service.imp.UserDetailServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthCreateUserRequest userRequest){
        return new ResponseEntity<>(this.userDetailService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest,
                                           HttpServletResponse response){
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest, response), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/web/login";
    }

}
