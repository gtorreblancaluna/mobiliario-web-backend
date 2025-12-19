package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.model.entitites.Abono;

public record AbonoResponseDTO(
        Integer id,
        Integer rentaId,
        Integer userId,
        String date,
        String paymentDate,
        Float payment,
        String comment,
        Integer typeId,
        String typeDescription // Nombre descriptivo del tipo (Ej: "Efectivo", "Transferencia")
) {
    /**
     * Mapea la entidad al Record DTO.
     */
    public static AbonoResponseDTO fromEntity(Abono entity) {
        return new AbonoResponseDTO(
                entity.getId(),
                entity.getRentaId(),
                entity.getUserId(),
                entity.getDate(),
                entity.getPaymentDate(),
                entity.getPayment(),
                entity.getComment(),
                (entity.getType() != null) ? entity.getType().getId() : null,
                (entity.getType() != null) ? entity.getType().getDescription() : null
        );
    }
}
