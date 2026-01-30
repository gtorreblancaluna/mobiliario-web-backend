package com.mx.gaby.mobiliario_web.model.entitites;

import com.mx.gaby.mobiliario_web.configs.converters.BooleanToStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@DynamicUpdate
@Table(name = "tasks_almacen")
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class AlmacenTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación con la tabla 'renta' (entidad Event)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renta_id", nullable = false)
    private Event event;

    // Catálogo de estados que creamos anteriormente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_almacen_tasks_catalog_id", nullable = false)
    private AlmacenTaskStatus status;

    // Catálogo de tipos de tarea que creamos anteriormente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attend_almacen_tasks_type_catalog_id", nullable = false)
    private AttendAlmacenTaskType type;

    // Usuario asignado por categoría (quien debe realizar la tarea)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_by_category_id", nullable = false)
    private User userByCategory;

    // Usuario que creó o gestiona la tarea
    // Se llena solo al insertar con el ID del usuario en sesión
    @CreatedBy
    @Column(name = "user_id", updatable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "fg_active", nullable = false,
            columnDefinition = "ENUM('1','0') DEFAULT '1'")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean fgActive = true;
}