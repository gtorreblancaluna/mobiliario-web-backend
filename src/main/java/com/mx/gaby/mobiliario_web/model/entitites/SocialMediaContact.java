package com.mx.gaby.mobiliario_web.model.entitites;

import com.mx.gaby.mobiliario_web.configs.converters.BooleanToStringConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "catalog_social_media_contact")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialMediaContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 145)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Usamos el conversor que creamos para manejarlo como boolean en Java
     * pero que se guarde como ENUM('1','0') en MySQL.
     */
    @Convert(converter = BooleanToStringConverter.class)
    @Column(name = "fg_active", nullable = false)
    private Boolean isActive = true;
}
