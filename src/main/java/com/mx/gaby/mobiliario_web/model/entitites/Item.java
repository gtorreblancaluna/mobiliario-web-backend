package com.mx.gaby.mobiliario_web.model.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "articulo")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario",
            nullable = false,
            referencedColumnName = "id_usuarios")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_categoria",
            nullable = false,
            referencedColumnName = "id_categoria")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_color",
            nullable = false,
            referencedColumnName = "id_color")
    private Color color;

    @Column(name = "cantidad")
    private Float amount;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "fecha_ingreso")
    private String createdAt;

    @Column(name = "precio_compra")
    private Float purchasePrice;

    @Column(name = "precio_renta")
    private Float rentaPrice;

    private Float stock;

    @Column(name = "codigo")
    private String code;

    @Column(name = "fecha_ultima_modificacion")
    private Timestamp updatedAt;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB", nullable = true)
    private byte[] image;

}
