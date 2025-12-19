package com.mx.gaby.mobiliario_web.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // 🛑 DEFINE EL ORIGEN DE PRODUCCIÓN DE TU FRONTEND DE CLOUD RUN
    private static final String FRONTEND_CLOUD_RUN_URL =
            "https://vue-app-mobiliario-1018293092710.us-central1.run.app";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Aplica esta configuración a todos los endpoints que empiecen con /api/
        registry.addMapping("/**")
                // 1. Dominio y puerto de tu Frontend (ej: Vue, React, Vite)
                .allowedOrigins(
                        "http://localhost:5173",
                        FRONTEND_CLOUD_RUN_URL)

                // 2. Métodos HTTP permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")

                // 3. Encabezados permitidos (incluyendo Authorization para JWT)
                .allowedHeaders("*")

                // 4. Permite el envío de cookies o credenciales (JWT, sesiones, etc.)
                .allowCredentials(true);
    }

}
