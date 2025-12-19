package com.mx.gaby.mobiliario_web.configs;

import com.mx.gaby.mobiliario_web.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final com.mx.gaby.mobiliario_web.filters.JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          com.mx.gaby.mobiliario_web.filters.JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // 1. Cifrador de Contraseñas (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Configurador de la Autenticación
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        // Este constructor se obtiene del objeto HttpSecurity
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                // 1. Especifica dónde buscar los detalles del usuario (tu servicio JPA)
                .userDetailsService(userDetailsService)

                // 2. Especifica el codificador para comparar el hash de la DB con la contraseña del login
                .passwordEncoder(passwordEncoder());

        // Construir y devolver la instancia de AuthenticationManager
        return authenticationManagerBuilder.build();
    }

    // 3. Cadena de Filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF (usando la nueva sintaxis con lambda)
                .csrf(AbstractHttpConfigurer::disable)

                // 💡 IMPORTANTE: Si tienes un Bean de CorsConfigurationSource (que Spring crea a partir de WebMvcConfigurer),
                // la forma más limpia es solo llamar a .cors() y Spring lo aplicará automáticamente.
                .cors(cors -> {}) // Simplemente llama a .cors() para integrar el CorsConfig Bean

                // 2. Configuración de Autorización: Usando authorizeHttpRequests
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Usar requestMatchers para las rutas públicas (sustituye a antMatchers)
                        .requestMatchers("/api/authenticate").permitAll()

                        // Requerir autenticación para el resto
                        .anyRequest().authenticated()
                )

                // 3. Configuración de Sesión: Sin estado (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // 4. Agregar el Filtro JWT antes del filtro de usuario/contraseña estándar
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
