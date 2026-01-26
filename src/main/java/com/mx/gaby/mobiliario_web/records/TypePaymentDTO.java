package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.model.entitites.TypePayment;

public record TypePaymentDTO(Integer id, String description) {

    public static TypePaymentDTO fromEntity (TypePayment typePayment) {
        return new TypePaymentDTO(
                typePayment.getId(),
                typePayment.getDescription()
        );
    }
}
