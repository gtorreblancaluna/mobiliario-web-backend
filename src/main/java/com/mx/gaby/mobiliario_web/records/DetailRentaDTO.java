package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;
import com.mx.gaby.mobiliario_web.model.entitites.Item;

/**
 * DTO para el detalle de la renta.
 * Incluimos el nombre del artículo para facilitar la visualización en la tabla de Vue.
 */
public record DetailRentaDTO(
        Integer id,
        Float amount,
        Integer itemId,
        String itemName, // Nombre extraído de la entidad Item
        Float unitPrice,
        String comment,
        Float discountPercentage,
        Float subtotal    // Calculado: (cantidad * precio) - descuento
) {

    public static DetailRenta fromDTO (DetailRentaDTO detailRentaDTO, Integer eventId) {

        DetailRenta detailRenta = new DetailRenta();

        Integer detailId = null;

        if (detailRentaDTO.id() != null
                && detailRentaDTO.id() > 0) {
            detailId = detailRentaDTO.id();
        }

        detailRenta.setId(detailId);
        detailRenta.setAmount(detailRentaDTO.amount());
        detailRenta.setEventId(eventId);

        Item item = new Item();
        item.setId(detailRentaDTO.itemId());
        detailRenta.setItem(item);

        detailRenta.setUnitPrice(detailRentaDTO.unitPrice());
        detailRenta.setComment(detailRentaDTO.comment());
        detailRenta.setDiscountPercentage(detailRentaDTO.discountPercentage());

        return detailRenta;

    }

    public static DetailRentaDTO fromEntity(DetailRenta entity) {

        float rawSubtotal = entity.getAmount() * entity.getUnitPrice();

        float discount = (entity.getDiscountPercentage() != null)
                ? (rawSubtotal * (entity.getDiscountPercentage() / 100)) : 0;

        return new DetailRentaDTO(
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
