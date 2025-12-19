package com.mx.gaby.mobiliario_web.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDTO(

        @NotBlank
        @Size(min = 3, max = 25)
        String username,

        @NotBlank
        @Size(min = 3, max = 20)
        String password) {
}
