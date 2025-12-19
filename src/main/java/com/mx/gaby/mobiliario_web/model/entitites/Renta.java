package com.mx.gaby.mobiliario_web.model.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "renta")
public class Renta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_renta")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_estado",
            nullable = false,
            referencedColumnName = "id_estado")
    private Estado state;

    @ManyToOne
    @JoinColumn(name = "id_clientes",
            nullable = false,
            referencedColumnName = "id_clientes")
    private Cliente customer;

    @ManyToOne
    @JoinColumn(name = "id_usuarios",
            nullable = false,
            referencedColumnName = "id_usuarios")
    private User user;

    private String fechaPedido;
    private String fechaEntrega;
    private String fechaEvento;
    private String horaEntrega;
    private String fechaDevolucion;
    private String horaDevolucion;
    private String descripcion;

    @Column(name = "descuento")
    private String porcentajeDescuento;
    private Float cantidadDescuento;
    private Float iva;

    @Column(length = 500)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_usuario_chofer",
            nullable = false,
            referencedColumnName = "id_usuarios")
    private User chofer;

    private Integer folio;

    @ManyToOne
    @JoinColumn(name = "id_tipo",
            nullable = false,
            referencedColumnName = "id_tipo")
    private Tipo tipo;

    private Float depositoGarantia;
    private Float envioRecoleccion;
    private String mostrarPreciosPdf;
    private Timestamp updatedAt;
    private Timestamp createdAt;




}
