package com.mx.gaby.mobiliario_web.exceptions;

public class BusinessException extends RuntimeException {

    // Constructor que acepta solo el mensaje de error
    public BusinessException(String message) {
        super(message);
    }

    // Constructor que acepta el mensaje y la causa original (stack trace)
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
