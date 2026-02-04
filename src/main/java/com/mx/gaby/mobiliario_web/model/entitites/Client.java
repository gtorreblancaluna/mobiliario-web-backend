package com.mx.gaby.mobiliario_web.model.entitites;

import com.mx.gaby.mobiliario_web.configs.converters.BooleanToStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "clientes")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clientes")
    private Integer id;

    @Column(name = "nombre", length = 45)
    private String firstName;

    @Column(name = "apellidos", length = 45)
    private String lastName;

    @Column(name = "apodo", length = 45)
    private String nickname;

    @Column(name = "tel_movil", length = 45)
    private String mobilePhone;

    @Column(name = "tel_fijo", length = 45)
    private String landlinePhone;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "direccion", length = 500)
    private String address;

    @Column(name = "localidad", length = 45)
    private String city; // O "locality" dependiendo de la precisión requerida

    @Column(name = "rfc", length = 45)
    private String taxId; // RFC es conocido como Tax ID en contextos internacionales

    @Column(name = "activo", nullable = false,
            columnDefinition = "ENUM('1','0')")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean isActive = true;

    @Column(name = "vip", nullable = false,
            columnDefinition = "ENUM('0','1')")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean isVip = true;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "catalog_social_media_contact_id", nullable = false)
    private Integer socialMediaContactId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
