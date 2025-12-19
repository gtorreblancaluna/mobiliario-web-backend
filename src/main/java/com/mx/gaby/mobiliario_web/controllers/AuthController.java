package com.mx.gaby.mobiliario_web.controllers;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.MessageConstant;
import com.mx.gaby.mobiliario_web.model.entitites.User;
import com.mx.gaby.mobiliario_web.model.responses.AuthenticationResponse;
import com.mx.gaby.mobiliario_web.model.responses.UserDataResponse;
import com.mx.gaby.mobiliario_web.records.AuthenticationRequestDTO;
import com.mx.gaby.mobiliario_web.services.UserDetailsServiceImpl;
import com.mx.gaby.mobiliario_web.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Log4j2
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(
            @Valid @RequestBody AuthenticationRequestDTO authRequest) throws Exception {

        log.info(LogConstant.INIT_AUTH, authRequest);

        try {
        // 1. Autentica las credenciales (Spring Security busca en JPA y compara BCrypt)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(),
                            authRequest.password())
            );
        } catch (AuthenticationException e) {
            // Capturar todas las fallas de autenticación (incluye BadCredentials y UserNotFound)
            log.error(LogConstant.USER_LOGIN_FAIL, authRequest.username());
            // Devolver un 401 (Unauthorized) controlado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(MessageConstant.USER_NOT_FOUND_LOGIN);
        }

        // 2. Si es exitoso, carga los detalles y genera el JWT
        final UserDetails userDetails
                = userDetailsService.loadUserByUsername(authRequest.username());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        User userAuthenticated = (User) userDetails;

        String nameUserAuthenticated = userAuthenticated.getName()
                + ApplicationConstant.BLANK_SPACE +
                userAuthenticated.getLastName();

        UserDataResponse userDataResponse =
                new UserDataResponse(nameUserAuthenticated,userAuthenticated.getPosition().getDescription());

        return ResponseEntity.ok(new AuthenticationResponse(jwt,userDataResponse));



    }

    @GetMapping("/hello")
    public String hello() {
        return "Acceso con JWT válido a recurso protegido.";
    }
}
