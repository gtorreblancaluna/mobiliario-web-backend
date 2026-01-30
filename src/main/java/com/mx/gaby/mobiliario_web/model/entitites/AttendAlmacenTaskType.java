package com.mx.gaby.mobiliario_web.model.entitites;

import com.mx.gaby.mobiliario_web.configs.converters.BooleanToStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "attend_almacen_tasks_type_catalog")
public class AttendAlmacenTaskType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 145)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    /**
     * fg_active mapeado para el ENUM('1','0') de MySQL.
     * Por defecto se inicializa en '1' (Activo).
     */
    @Column(name = "fg_active", nullable = false,
            columnDefinition = "ENUM('1','0') DEFAULT '1'")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean fgActive = true;
}