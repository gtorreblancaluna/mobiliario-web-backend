package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.model.entitites.SocialMediaContact;

import java.io.Serializable;

/**
 * Immutable DTO for social media catalog data.
 * Optimized for dropdown lists and select components in Vue 3.
 */
public record SocialMediaContactRecord(
        Integer id,
        String description
) implements Serializable {

    /**
     * Factory method to convert Entity to Record.
     */
    public static SocialMediaContactRecord fromEntity(SocialMediaContact entity) {
        if (entity == null) return null;

        return new SocialMediaContactRecord(
                entity.getId(),
                entity.getDescription()
        );
    }

    /**
     * Factory method to convert Record to Entity.
     */
    public static SocialMediaContact fromDTO(SocialMediaContactRecord dto) {
        if (dto == null) return null;

        SocialMediaContact entity = new SocialMediaContact();
        entity.setId(dto.id());
        entity.setDescription(dto.description());

        return entity;
    }
}
