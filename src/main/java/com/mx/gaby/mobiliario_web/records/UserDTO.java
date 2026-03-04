package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.model.entitites.Position;
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

    /**
     * Mapeo de Record (DTO) a Entidad (User)
     */
    public static User fromDTO(UserDTO userDTO) {

        if (userDTO == null) return null;

        User user = new User();
        user.setId(userDTO.id());
        user.setName(userDTO.name());
        user.setLastName(userDTO.lastName());
        user.setUsername(userDTO.username());
        user.setTelMovil(userDTO.telMovil());
        user.setTelFijo(userDTO.telFijo());
        user.setDireccion(userDTO.direccion());
        user.setAdmin(userDTO.isAdmin());
        user.setFgWarehouseManager(userDTO.isWarehouseManager());
        user.setFgActive(userDTO.fgActive());

        // Manejo de la relación Position para evitar TransientPropertyValueException
        if (userDTO.positionId() != null) {
            Position position = new Position();
            position.setId(userDTO.positionId());
            // Solo seteamos el ID, que es lo que JPA necesita para la FK
            user.setPosition(position);
        }

        return user;
    }
}