package com.mx.gaby.mobiliario_web.model.entitites;

import com.mx.gaby.mobiliario_web.configs.converters.BooleanToStringConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuarios")
    private Integer id;
    @Column(name = "nombre")
    private String name;
    @Column(name = "apellidos")
    private String lastName;
    private String username;
    private String telMovil;
    private String telFijo;
    private String direccion;

    @Column(name = "administrador")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean admin;

    @Column(name = "fg_encargado_almacen")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean fgWarehouseManager;

    private String nivel1;
    private String nivel2;
    @Column(name = "contrasenia")
    private String password;

    @Column(name = "activo", nullable = false)
    @Convert(converter = BooleanToStringConverter.class)
    private boolean fgActive = true;

    @ManyToOne
    @JoinColumn(name = "id_puesto",
            nullable = false,
            referencedColumnName = "id_puesto"  )
    private Position position;

    // Métodos de UserDetails (implementación mínima para Spring Security)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // En un caso real, devolveríamos los roles del usuario.
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Simplificación: Siempre activo, no expirado, no bloqueado
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

}