package com.mx.gaby.mobiliario_web.model.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "abonos")
public class Abono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abonos")
    private Integer id;

    @Column(name = "id_renta")
    private Integer rentaId;

    @Column(name = "id_usuario")
    private Integer userId;

    @Column(name = "fecha")
    private String date;

    @Column(name = "fecha_pago")
    private String paymentDate;

    @Column(name = "abono")
    private Float payment;

    @Column (name = "comentario")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "id_tipo_abono",
            nullable = false,
            referencedColumnName = "id_tipo_abono")
    private TipoAbono type;

}
