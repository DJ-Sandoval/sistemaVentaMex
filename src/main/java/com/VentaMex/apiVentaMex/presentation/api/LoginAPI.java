package com.VentaMex.apiVentaMex.presentation.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(LoginAPI.BASE_URL)
public interface LoginAPI {
    String BASE_URL="/access";

    @GetMapping("/logout")
    String logout(HttpServletResponse response);
}
