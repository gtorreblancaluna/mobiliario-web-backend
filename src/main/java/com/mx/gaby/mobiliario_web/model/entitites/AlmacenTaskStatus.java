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
@Table(name = "status_almacen_tasks_catalog")
public class AlmacenTaskStatus {

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
     * fg_active mapeado como String para manejar el ENUM('1','0') de MySQL.
     * El valor por defecto se maneja en la lógica o mediante la definición de la columna.
     */
    @Column(name = "fg_active", nullable = false,
            columnDefinition = "ENUM('1','0') DEFAULT '1'")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean fgActive = true;
}