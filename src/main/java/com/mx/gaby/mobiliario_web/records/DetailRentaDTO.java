package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.model.entitites.EventDetail;
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

    public static EventDetail fromDTO (DetailRentaDTO detailRentaDTO, Integer eventId) {

        EventDetail eventDetail = new EventDetail();

        Integer detailId = null;

        if (detailRentaDTO.id() != null
                && detailRentaDTO.id() > 0) {
            detailId = detailRentaDTO.id();
        }

        eventDetail.setId(detailId);
        eventDetail.setAmount(detailRentaDTO.amount());
        eventDetail.setEventId(eventId);

        Item item = new Item();
        item.setId(detailRentaDTO.itemId());
        eventDetail.setItem(item);

        eventDetail.setUnitPrice(detailRentaDTO.unitPrice());
        eventDetail.setComment(detailRentaDTO.comment());
        eventDetail.setDiscountPercentage(detailRentaDTO.discountPercentage());

        return eventDetail;

    }

    public static DetailRentaDTO fromEntity(EventDetail entity) {

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
