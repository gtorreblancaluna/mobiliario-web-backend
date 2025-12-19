package com.mx.gaby.mobiliario_web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para cuando un recurso (Renta, Artículo, etc.)
 * no existe en la base de datos.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
