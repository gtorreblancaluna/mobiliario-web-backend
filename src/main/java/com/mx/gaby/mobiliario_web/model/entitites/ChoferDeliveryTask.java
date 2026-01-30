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
@Table(name = "tasks_chofer_delivery")
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
public class ChoferDeliveryTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renta_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_almacen_tasks_catalog_id", nullable = false)
    private AlmacenTaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attend_almacen_tasks_type_catalog_id", nullable = false)
    private AttendAlmacenTaskType type;

    // El chofer asignado (de la tabla usuarios)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chofer_id", nullable = false)
    private User chofer;

    // Usuario que registró la tarea de entrega
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