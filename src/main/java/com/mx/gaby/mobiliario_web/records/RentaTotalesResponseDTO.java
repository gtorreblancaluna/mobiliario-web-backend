package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;
import com.mx.gaby.mobiliario_web.model.entitites.Event;

import java.util.List;

public record RentaTotalesResponseDTO(
        Float total,
        Float totalIva,
        Float totalDiscount,
        Float totalItems,
        Float totalPayments
) {

    public static RentaTotalesResponseDTO calculateTotals
            (Event event,
             List<DetailRenta> detail, List<PaymentDTO> payments) {

        float subTotalItems = 0F;
        float totalDiscount = 0F;
        float calculoIVA = 0f;
        float totalCalculoConIVA;

        float envioRecoleccion =
                event.getEnvioRecoleccion() != null ? event.getEnvioRecoleccion() : 0F;
        float depositoGarantia =
                event.getDepositoGarantia() != null ? event.getDepositoGarantia() : 0F;

        float totalPayments = payments.stream()
                .map(PaymentDTO::amount) // Extrae el valor del abono
                .filter(p -> p != null)        // Seguridad: evita NullPointerException
                .reduce(0f, Float::sum);       // Suma todos los valores

        for (DetailRenta detailRenta : detail) {

            float subtotalByItem =
                    detailRenta.getAmount() * detailRenta.getUnitPrice();

            float totalDiscountByItem = 0F;

            if (detailRenta.getDiscountPercentage() != null
                    && detailRenta.getDiscountPercentage() > 0F ) {
                totalDiscountByItem
                        = subtotalByItem * (detailRenta.getDiscountPercentage() / 100f);
            }
            subTotalItems += subtotalByItem - totalDiscountByItem;
        }

        if (event.getPorcentajeDescuento() != null) {
            float percentage = Float.parseFloat(event.getPorcentajeDescuento());
            if (percentage > 0F) {
                totalDiscount = subTotalItems * (percentage / 100);
            }
        }

        float calculoSinIVA = (subTotalItems + envioRecoleccion + depositoGarantia) - totalDiscount;

        if (event.getIva() != null && event.getIva() > 0F) {
            calculoIVA = calculoSinIVA * (event.getIva() / 100);
        }

        totalCalculoConIVA = (subTotalItems + envioRecoleccion + depositoGarantia + calculoIVA) - totalDiscount;

        float total = totalCalculoConIVA - totalPayments;

        return new RentaTotalesResponseDTO(
                total,
                calculoIVA,
                totalDiscount,
                subTotalItems,
                totalPayments
        );
    }

}
