package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.model.entitites.Item;

import java.io.Serializable;

/**
 * DTO representativo para el catálogo de artículos.
 * Optimizado para el autocompletado en el frontend.
 */
public record ItemDTO(
        Integer id,
        String itemName,     // Mapea a description de la entidad
        Float unitPrice,     // Mapea a rentaPrice o purchasePrice según tu lógica
        String code,
        String color,
        Float stock,
        Integer categoryId,
        String categoryName
) implements Serializable {
    public static ItemDTO fromEntity (Item item) {
        return new ItemDTO(
                item.getId(),
                item.getDescription(), // itemName en el front
                item.getRentaPrice(),  // unitPrice en el front
                item.getCode(),
                item.getColor() != null ? item.getColor().getColor() : ApplicationConstant.EMPTY_STRING,
                item.getStock(),
                item.getCategory() != null ? item.getCategory().getId() : null,
                item.getCategory() != null ? item.getCategory().getDescription() : ApplicationConstant.EMPTY_STRING
        );
    }
}
