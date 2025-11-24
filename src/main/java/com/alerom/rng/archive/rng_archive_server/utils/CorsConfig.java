package com.alerom.rng.archive.rng_archive_server.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica a todas las rutas que comiencen con /api
                .allowedOrigins("http://localhost:4200") // El origen de tu app Angular
                .allowedMethods("*") // Permite todos los métodos (GET, POST, etc.)
                .allowedHeaders("*") // Permite todas las cabeceras
                .allowCredentials(true); // Si utilizas sesiones o JWTs con credenciales
    }
}