package com.mx.gaby.mobiliario_web.model.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "detalle_renta")
public class DetailRenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_renta")
    private Integer id;

    @Column(name = "cantidad")
    private Float amount;

    @Column(name = "id_renta")
    private Integer eventId;

    @ManyToOne
    @JoinColumn(name = "id_articulo",
            nullable = false,
            referencedColumnName = "id_articulo")
    private Item item;

    @Column(name = "p_unitario")
    private Float unitPrice;

    @Column(name = "comentario")
    private String comment;

    @Column(name = "porcentaje_descuento")
    private Float discountPercentage;

}
