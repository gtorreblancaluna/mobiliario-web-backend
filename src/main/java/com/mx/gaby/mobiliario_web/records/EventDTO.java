package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.model.entitites.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

// Utilizamos record para un DTO inmutable y conciso.
public record EventDTO(
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
        Integer tipoId
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
     * @param eventDTO La entidad Renta.
     * @return RentaResponseDTO.
     */

    public static Event fromDTO (EventDTO eventDTO) {

        Event event = new Event();

        event.setId(eventDTO.id());

        Estado estado = new Estado();
        estado.setId(eventDTO.estadoId());
        event.setState(estado);

        Customer customer = new Customer();
        customer.setId(eventDTO.clienteId());
        event.setCustomer(customer);

        User user = new User();
        user.setId(eventDTO.usuarioId());
        event.setUser(user);

        event.setFechaEntrega(eventDTO.fechaEntrega());
        event.setFechaEvento(eventDTO.fechaEvento());
        event.setHoraEntrega(eventDTO.horaEntrega());
        event.setFechaDevolucion(eventDTO.fechaDevolucion());
        event.setFechaPedido(eventDTO.fechaPedido());
        event.setHoraDevolucion(eventDTO.horaDevolucion());
        event.setDescripcion(eventDTO.descripcion());

        event.setPorcentajeDescuento(eventDTO.porcentajeDescuento());
        event.setIva(eventDTO.iva());

        event.setComentario(eventDTO.comentario());

        User chofer = new User();
        chofer.setId(eventDTO.choferId());
        event.setChofer(chofer);

        TypeRenta type = new TypeRenta();
        type.setId(eventDTO.tipoId());
        event.setType(type);

        event.setDepositoGarantia(eventDTO.depositoGarantia());
        event.setEnvioRecoleccion(eventDTO.envioRecoleccion());
        event.setMostrarPreciosPdf(eventDTO.mostrarPrecios());

        event.setCreatedAt(eventDTO.createdAt());
        event.setUpdatedAt(eventDTO.updatedAt());
        event.setFolio(eventDTO.folio());


        return event;
    }

    public static EventDTO fromEntity(Event event) {
        return new EventDTO(
                event.getId(),
                event.getState() != null ? event.getState().getId() : null,
                event.getState() != null ? event.getState().getDescription() : null,
                event.getCustomer() != null ? event.getCustomer().getId() : null,
                event.getCustomer() != null ? event.getCustomer().getName()
                        + ApplicationConstant.BLANK_SPACE + event.getCustomer().getLastName(): null,
                event.getUser() != null ? event.getUser().getId() : null,
                event.getUser() != null ? event.getUser().getName()
                        + ApplicationConstant.BLANK_SPACE + event.getUser().getLastName() : null,

                event.getChofer() != null ? event.getChofer().getId() : null,

                event.getChofer() != null ? event.getChofer().getName()
                        + ApplicationConstant.BLANK_SPACE + event.getChofer().getLastName() : null,

                event.getFechaPedido(),
                event.getFechaEntrega(),
                event.getFechaEvento(),
                event.getHoraEntrega(),
                event.getFechaDevolucion(),
                event.getHoraDevolucion(),

                event.getDescripcion(),
                event.getPorcentajeDescuento(),
                event.getCantidadDescuento(),
                event.getIva(),
                event.getComentario(),
                event.getFolio(),

                event.getDepositoGarantia(),
                event.getEnvioRecoleccion(),
                event.getMostrarPreciosPdf(),

                event.getUpdatedAt(),
                event.getCreatedAt(),
                event.getType().getTipo(),
                event.getType().getId()
        );
    }


}