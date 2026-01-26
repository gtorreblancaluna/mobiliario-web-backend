package com.mx.gaby.mobiliario_web.model.entitites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "abonos")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abonos")
    private Integer id;

    @Column(name = "id_renta")
    private Integer rentaId;

    @ManyToOne
    @JoinColumn(name = "id_usuario",
            nullable = false,
            referencedColumnName = "id_usuarios")
    private User user;

    @Column(name = "fecha")
    private String date;

    @Column(name = "fecha_pago")
    private String paymentDate;

    @Column(name = "abono")
    private Float amount;

    @Column (name = "comentario")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "id_tipo_abono",
            nullable = false,
            referencedColumnName = "id_tipo_abono")
    private TypePayment type;

    private Timestamp updatedAt;
    private Timestamp createdAt;

}
