package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.model.entitites.Renta;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

// Utilizamos record para un DTO inmutable y conciso.
public record RentaResponseDTO(
        Integer id,
        Integer estadoId,          // ID del estado (ej. Pendiente, Entregado)
        String estadoDescription,       // Nombre del estado (ej. "Pendiente")
        Integer clienteId,            // ID del cliente
        String clienteNombre,      // Nombre/Identificador del cliente
        Integer usuarioId,            // ID del usuario que registró la renta
        String userName,
        Integer choferId,
        String choferName,

        // Fechas y Horas
        String fechaPedido,
        String fechaEntrega,
        String fechaEvento,
        String horaEntrega,
        String fechaDevolucion,
        String horaDevolucion,

        // Detalles
        String descripcion,
        String porcentajeDescuento,
        Float cantidadDescuento,
        Float iva,
        String comentario,
        Integer folio,

        // Costos
        Float depositoGarantia,
        Float envioRecoleccion,
        String mostrarPrecios,

        // Información de auditoría
        Timestamp updatedAt,
        Timestamp createdAt,
        String tipo,
        List<DetailRentaResponseDTO> detail,
        RentaTotalesResponseDTO totals
) {

    public static String formatDate (String initDateString) {

        try {
            Locale spanishLocale
                    = Locale.of(ApplicationConstant.LANGUAGE, ApplicationConstant.COUNTRY);

            DateTimeFormatter initFormat =
                    DateTimeFormatter.ofPattern(ApplicationConstant.FORMAT_DATE_DD_MM_YYY);

            LocalDate localDate = LocalDate.parse(initDateString, initFormat);

            DateTimeFormatter resultFormat =
                    DateTimeFormatter.ofPattern(ApplicationConstant.FORMAT_DATE_SHOW_IN_FRONT, spanishLocale);

            return localDate.format(resultFormat);

        } catch (DateTimeParseException e) {
            // Manejo específico del error si el formato de entrada es incorrecto
            return "ERROR: Formato de fecha de entrada inválido. Se esperaba " + ApplicationConstant.FORMAT_DATE_DD_MM_YYY;
        }
    }

    /**
     * Método estático para mapear la Entidad Renta al DTO de Respuesta.
     * @param renta La entidad Renta.
     * @return RentaResponseDTO.
     */

    public static RentaResponseDTO fromEntity(Renta renta,
                                              List<DetailRentaResponseDTO> detail,
                                              RentaTotalesResponseDTO totals) {
        return new RentaResponseDTO(
                renta.getId(),
                renta.getState() != null ? renta.getState().getId() : null,
                renta.getState() != null ? renta.getState().getDescription() : null,
                renta.getCustomer() != null ? renta.getCustomer().getId() : null,
                renta.getCustomer() != null ? renta.getCustomer().getName()
                        + ApplicationConstant.BLANK_SPACE + renta.getCustomer().getLastName(): null,
                renta.getUser() != null ? renta.getUser().getId() : null,
                renta.getUser() != null ? renta.getUser().getName()
                        + ApplicationConstant.BLANK_SPACE + renta.getUser().getLastName() : null,

                renta.getChofer() != null ? renta.getChofer().getId() : null,

                renta.getChofer() != null ? renta.getChofer().getName()
                        + ApplicationConstant.BLANK_SPACE + renta.getChofer().getLastName() : null,

                formatDate(renta.getFechaPedido()),
                formatDate(renta.getFechaEntrega()),
                formatDate(renta.getFechaEvento()),
                renta.getHoraEntrega(),
                formatDate(renta.getFechaDevolucion()),
                renta.getHoraDevolucion(),

                renta.getDescripcion(),
                renta.getPorcentajeDescuento(),
                renta.getCantidadDescuento(),
                renta.getIva(),
                renta.getComentario(),
                renta.getFolio(),

                renta.getDepositoGarantia(),
                renta.getEnvioRecoleccion(),
                renta.getMostrarPreciosPdf(),

                renta.getUpdatedAt(),
                renta.getCreatedAt(),
                renta.getTipo().getTipo(),
                detail,
                totals
        );
    }


}