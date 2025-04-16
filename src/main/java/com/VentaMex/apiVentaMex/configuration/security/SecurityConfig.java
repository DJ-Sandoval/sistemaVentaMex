package com.VentaMex.apiVentaMex.configuration.security;
import com.VentaMex.apiVentaMex.configuration.filter.JwtTokenValidator;
import com.VentaMex.apiVentaMex.service.imp.UserDetailServiceImpl;
import com.VentaMex.apiVentaMex.util.components.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // CSRF desactivado, pero revisa tu caso de uso.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // EndPoints públicos
                    http.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll(); // Rutas Swagger
                    http.requestMatchers(HttpMethod.POST, "/auth/**").permitAll(); // Rutas de login
                    http.requestMatchers("/actuator/**").permitAll(); // Actuator
                    http.requestMatchers("/oauth2/**", "/login/oauth2/code/**", "/css/**", "/js/**", "/img/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/clientes/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/productos/**").permitAll();
                    http.requestMatchers(HttpMethod.PUT, "/api/productos/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/ventas/**").permitAll();
                    http.requestMatchers(HttpMethod.POST, "/api/ventas/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/web/**").permitAll();

                    // EndPoints Privados (Roles específicos)
                    http.requestMatchers(HttpMethod.GET, "/method/get").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.POST, "/method/post").hasRole("USER");
                    http.requestMatchers(HttpMethod.DELETE, "/method/delete").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/method/put").hasRole("USER");
//.hasAuthority("ROLE_USER");
                    http.requestMatchers("/api/clientes/**").hasRole("DEVELOPER");
                    http.requestMatchers("/web/**").hasAuthority("DEVELOPER");

                    // Denegar acceso a cualquier otra ruta no especificada
                    http.anyRequest().denyAll();
                })
                .oauth2Login(oauth2 ->
                                oauth2
                                        .loginPage("/auth/login") // tu página de login personalizada
                                        .defaultSuccessUrl("/web/home", true) // redirige tras login OAuth
                        .failureUrl("/auth/login?error=true") // opcional para manejar errores
                )
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

