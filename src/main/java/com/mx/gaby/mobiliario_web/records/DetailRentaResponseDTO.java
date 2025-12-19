package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;

/**
 * DTO para el detalle de la renta.
 * Incluimos el nombre del artículo para facilitar la visualización en la tabla de Vue.
 */
public record DetailRentaResponseDTO(
        Integer id,
        Float amount,
        Integer itemId,
        String itemName, // Nombre extraído de la entidad Item
        Float unitPrice,
        String comment,
        Float discountPercentage,
        Float subtotal    // Calculado: (cantidad * precio) - descuento
) {

    public static DetailRentaResponseDTO fromEntity(DetailRenta entity) {

        float rawSubtotal = entity.getAmount() * entity.getUnitPrice();

        float discount = (entity.getDiscountPercentage() != null)
                ? (rawSubtotal * (entity.getDiscountPercentage() / 100)) : 0;

        return new DetailRentaResponseDTO(
                entity.getId(),
                entity.getAmount(),
                entity.getItem().getId(), // Asumiendo que Item tiene getId()
                entity.getItem().getDescription(), // Asumiendo que Item tiene getName() o getDescripcion()
                entity.getUnitPrice(),
                entity.getComment(),
                entity.getDiscountPercentage(),
                (rawSubtotal - discount)
        );
    }
}
