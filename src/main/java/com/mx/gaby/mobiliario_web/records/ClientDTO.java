package com.mx.gaby.mobiliario_web.records;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.model.entitites.Client;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clean and immutable DTO for Client data.
 * Java Records are ideal for high-performance data transfer in Spring Boot.
 */
public record ClientDTO (
        Integer id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 145)
        String firstName,

        @NotBlank(message = "Apellidos son obligatorio")
        @Size(max = 145)
        String lastName,
        String fullName,
        String nickname,

        @Pattern(regexp = "^\\d{10}$", message = "El teléfono movil debe tener 10 dígitos")
        String mobilePhone,

        String landlinePhone,

        @Email(message = "Formato de email inválido")
        String email,
        String address,
        String city,

        String rfc,        // RFC
        Boolean isActive,     // ENUM('1','0')
        Boolean isVip,        // ENUM('1','0')

        @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
        LocalDate birthday,
        Integer socialMediaContactId

) implements Serializable {

    /**
     * Factory method to transform a JPA Entity into an immutable Record.
     * Ideal for use in Service layers or Repositories.
     */
    public static ClientDTO fromEntity(Client entity) {
        if (entity == null) {
            return null;
        }

        return new ClientDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getFirstName()
                        + ApplicationConstant.BLANK_SPACE + entity.getLastName(),
                entity.getNickname(),
                entity.getMobilePhone(),
                entity.getLandlinePhone(),
                entity.getEmail(),
                entity.getAddress(),
                entity.getCity(),
                entity.getTaxId(),
                entity.isActive(),
                entity.isVip(),
                entity.getBirthday(),
                entity.getSocialMediaContactId()
        );
    }

    /**
     * Factory method to create an Entity from a DTO (Record).
     * Useful for saving or updating records coming from the Frontend.
     */
    public static Client fromDTO(ClientDTO dto) {
        if (dto == null) {
            return null;
        }

        Client client = new Client();

        // Mapeo de campos
        client.setId(dto.id());
        client.setFirstName(dto.firstName());
        client.setLastName(dto.lastName());
        client.setNickname(dto.nickname());
        client.setMobilePhone(dto.mobilePhone());
        client.setLandlinePhone(dto.landlinePhone());
        client.setEmail(dto.email());
        client.setAddress(dto.address());
        client.setCity(dto.city());
        client.setTaxId(dto.rfc());

        // Si el DTO trae null, asignamos el valor por defecto directamente
        client.setActive(Boolean.TRUE.equals(dto.isActive()));
        client.setVip(Boolean.TRUE.equals(dto.isVip()));

        client.setBirthday(dto.birthday());
        client.setSocialMediaContactId(dto.socialMediaContactId());

        return client;
    }

}