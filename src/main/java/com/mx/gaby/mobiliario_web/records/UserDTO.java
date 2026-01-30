package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.model.entitites.User;

public record UserDTO(
        Integer id,
        String name,
        String lastName,
        String fullName,
        String username,
        String telMovil,
        String telFijo,
        String direccion,
        boolean isAdmin,
        boolean isWarehouseManager,
        boolean fgActive,
        Integer positionId,
        String positionDescription
) {
    /**
     * Mapeo de Entidad a Record (DTO)
     */
    public static UserDTO fromEntity(User user) {
        if (user == null) return null;

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getName() + ApplicationConstant.BLANK_SPACE + user.getLastName(),
                user.getUsername(),
                user.getTelMovil(),
                user.getTelFijo(),
                user.getDireccion(),
                user.isAdmin(),
                user.isFgWarehouseManager(),
                user.isFgActive(),
                user.getPosition() != null ? user.getPosition().getId() : null,
                user.getPosition() != null ? user.getPosition().getDescription() : "Sin Puesto"
        );
    }
}