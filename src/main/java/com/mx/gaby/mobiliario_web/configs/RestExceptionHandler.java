package com.mx.gaby.mobiliario_web.configs;

import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.exceptions.NotFoundException;
import com.mx.gaby.mobiliario_web.records.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    // Este metodo atrapa CUALQUIER excepción no controlada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception ex) {

        // 1. Logueamos el error interno para nosotros (en la consola de Spring)
        logger.error("Error no controlado: ", ex);

        // 2. Creamos la respuesta amigable para el cliente (Vue.js)

        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneId.systemDefault()
        );

        ErrorResponse error = new ErrorResponse(
                "Ocurrió un error interno en el servidor. Por favor, intente más tarde.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                dateTime
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        Map<String, Object> errorDetails = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "No encontrado",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja las BusinessException, devolviendo un 422 Unprocessable entity.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {

        // Puedes devolver un JSON estructurado o solo el mensaje
        Map<String, String> errorDetails = Map.of(
                "status", String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                "error", "Error de Lógica de Negocio",
                "message", ex.getMessage() // El mensaje que definiste al lanzar la excepción
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    /**
     * Maneja los errores de validación de @RequestBody (DTOs)
     * Cuando se usa @Valid en un parámetro de un controlador.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        // 1. Recolectar todos los errores de campo
        Map<String, String> errors = new HashMap<>();

        // El método getFieldErrors() obtiene los errores específicos por campo
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            // Clave: Nombre del campo (ej: 'nombre')
            String fieldName = error.getField();
            // Valor: Mensaje de error (ej: 'El nombre es obligatorio.')
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // 2. Devolver una respuesta HTTP 400 (Bad Request) con los errores
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // --- Opcional: Manejar errores de @PathVariable, @RequestParam, etc. ---

    /**
     * Maneja los errores de validación a nivel de método (@PathVariable, @RequestParam)
     * Cuando se usa @Validated en la clase del controlador.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        // Clave: Path (ej: 'crearProducto.id')
                        violation -> violation.getPropertyPath().toString(),
                        // Valor: Mensaje de error
                        violation -> violation.getMessage()
                ));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
