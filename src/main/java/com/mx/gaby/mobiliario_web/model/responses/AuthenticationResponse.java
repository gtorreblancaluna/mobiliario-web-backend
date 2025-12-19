package com.mx.gaby.mobiliario_web.model.responses;

/**
 * @param jwt El campo clave que contiene el token JWT generado
 */
public record AuthenticationResponse(
        String jwt,
        UserDataResponse userData)
{

}
