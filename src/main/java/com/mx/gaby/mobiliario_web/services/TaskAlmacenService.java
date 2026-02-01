package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.*;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Log4j2
public abstract class TaskAlmacenService {

    protected boolean checkIfItemsHasBeenUpdated(
            final List<DetailRentaDTO> detailToUpdate,
            final List<DetailRentaDTO> currentDetail) {

        // 1. Cláusula de guarda: Si uno es nulo o tienen distinto tamaño, hubo cambios
        if (detailToUpdate == null
                || currentDetail == null
                || detailToUpdate.size() != currentDetail.size()) {
            return true;
        }

        // 2. Convertimos la lista "nueva" a un Map para búsquedas O(1)
        // Clave: ID del artículo, Valor: Cantidad
        Map<Integer, Float> eventToUpdateMap = detailToUpdate.stream()
                .collect(Collectors.toMap(
                        DetailRentaDTO::itemId,
                        DetailRentaDTO::amount,
                        (existing, replacement) -> existing // Merge function: keep the first one found
                ));

        // 3. Verificamos si todos los artículos actuales existen en el nuevo y tienen la misma cantidad
        return currentDetail.stream().anyMatch(actual -> {
            Float newAmount = eventToUpdateMap.get(actual.itemId());

            // Si no existe el ID o la cantidad es diferente, entonces se actualizó (true)
            return newAmount == null || !newAmount.equals(actual.amount());
        });
    }

    protected ChoferDeliveryTask getChoferDeliveryTask (
            EventDTO eventToUpdate,StatusTask statusTask, Integer choferId) {

        ChoferDeliveryTask choferDeliveryTask = new ChoferDeliveryTask();

        // Event
        Event event = new Event();
        event.setId(eventToUpdate.id());
        choferDeliveryTask.setEvent(event);

        // status
        AlmacenTaskStatus almacenTaskStatus = new AlmacenTaskStatus();
        almacenTaskStatus.setId(statusTask.getId());
        choferDeliveryTask.setStatus(almacenTaskStatus);

        //type
        AttendAlmacenTaskType attendAlmacenTaskType = new AttendAlmacenTaskType();
        attendAlmacenTaskType.setId(1);
        choferDeliveryTask.setType(attendAlmacenTaskType);

        User chofer = new User();
        chofer.setId(choferId);
        choferDeliveryTask.setChofer(chofer);

        chofer.setFgActive(true);

        return choferDeliveryTask;
    }

    protected boolean checkIfGeneralDataHasBeenUpdated(
            final EventDTO eventToUpdate, final EventDTO currentEvent) {

        return !Objects.equals(eventToUpdate.horaDevolucion(), currentEvent.horaDevolucion())
                || !Objects.equals(eventToUpdate.horaEntrega(), currentEvent.horaEntrega())
                || !Objects.equals(eventToUpdate.fechaEntrega(), currentEvent.fechaEntrega())
                || !Objects.equals(eventToUpdate.fechaDevolucion(), currentEvent.fechaDevolucion())
                || !Objects.equals(eventToUpdate.fechaEvento(), currentEvent.fechaEvento())
                || !Objects.equals(eventToUpdate.comentario(), currentEvent.comentario())
                || !Objects.equals(eventToUpdate.descripcion(), currentEvent.descripcion());
    }


    protected StatusTask applyRulesAndGetStatus (
            final EventDTO currentEvent,
            final EventDTO eventToUpdate,
            final List<DetailRentaDTO> detailToUpdate,
            final List<DetailRentaDTO> currentDetail) throws BusinessException{

        boolean itemsHasBeenUpdates = checkIfItemsHasBeenUpdated(
                detailToUpdate,
                currentDetail
        );

        boolean generalDataHasBeenupdated = checkIfGeneralDataHasBeenUpdated(
                eventToUpdate,currentEvent
        );

        if (eventToUpdate.tipoId().toString().equals(ApplicationConstant.TIPO_COTIZACION)
                && !eventToUpdate.estadoId().toString().equals(ApplicationConstant.ESTADO_PENDIENTE)) {
            throw new BusinessException("""
                    No se generaron tareas para el folio %s.
                    Al ser de tipo %s, debe tener un Estado igual a %s.
                    """.formatted(
                    currentEvent.folio(),
                    ApplicationConstant.DS_TIPO_COTIZACION,
                    ApplicationConstant.DS_ESTADO_PENDIENTE
            ));
        }

        // 1. Extraemos variables booleanas para que los IFs se lean como lenguaje natural
        String typeId = eventToUpdate.tipoId().toString();
        String statusId = eventToUpdate.estadoId().toString();
        String currentTypeId = currentEvent.tipoId().toString();
        String currentStatusId = currentEvent.estadoId().toString();

        boolean isApartado = statusId.equals(ApplicationConstant.ESTADO_APARTADO);
        boolean isPedidoOrFabricacion = typeId.equals(ApplicationConstant.TIPO_PEDIDO)
                || typeId.equals(ApplicationConstant.TIPO_FABRICACION);

        boolean typeChanged = !typeId.equals(currentTypeId);
        boolean statusChanged = !statusId.equals(currentStatusId);

        StatusTask resultStatus = null;

        if (generalDataHasBeenupdated && isPedidoOrFabricacion && isApartado) {
            resultStatus = StatusTask.GENERAL_DATA_UPDATED;
        }
        else if (itemsHasBeenUpdates && isPedidoOrFabricacion && isApartado) {
            resultStatus = StatusTask.UPDATE_ITEMS;
        }
        else if (typeChanged && statusChanged) {
            resultStatus = StatusTask.UPDATE_TYPE_AND_STATUS;
        }
        else if (typeChanged) {
            resultStatus = StatusTask.UPDATE_TYPE;
        }
        else if (statusChanged && !statusId.equals(ApplicationConstant.ESTADO_PENDIENTE)) {
            resultStatus = StatusTask.UPDATE_STATUS;
        }

        // 3. Verificación final y manejo de excepción
        if (resultStatus != null) {
            return resultStatus;
        } else {

            String rulesActive = """
                1. Cambio en los datos generales y el folio es de tipo: %s
                2. Cambio en los datos generales y el folio es de tipo: %s y estado: %s.
                3. Cambio en artículos y el folio es de tipo: %s y estado: %s.
                4. Cambio en artículos y el folio es de tipo: %s y estado: %s.
                5. Cambio el estado del folio diferente a: %s
                6. Cambio el tipo del folio.
                7. Cambio el estado y tipo del folio.
                """.formatted(
                    ApplicationConstant.DS_TIPO_PEDIDO,
                    ApplicationConstant.DS_TIPO_FABRICACION,
                    ApplicationConstant.DS_ESTADO_APARTADO,
                    ApplicationConstant.DS_TIPO_PEDIDO,
                    ApplicationConstant.DS_ESTADO_APARTADO,
                    ApplicationConstant.DS_TIPO_FABRICACION,
                    ApplicationConstant.DS_ESTADO_APARTADO,
                    ApplicationConstant.DS_ESTADO_PENDIENTE
            );

            throw new BusinessException("""
                No se generaron tareas, ya que no coincidió con las reglas operativas actuales...
                Reglas operativas aplicadas: %s
                """.formatted(rulesActive));
        }

    }

    // El Enum vive dentro de la clase para mantener el contexto
    @Getter
    public enum StatusTask {
        NEW(1, ApplicationConstant.TASK_NEW_FOLIO),
        UPDATE_STATUS(2, ApplicationConstant.TASK_UPDATE_STATUS_FOLIO),
        UPDATE_TYPE(3, ApplicationConstant.TASK_UPDATE_TYPE_FOLIO),
        UPDATE_TYPE_AND_STATUS(4, ApplicationConstant.TASK_UPDATE_TYPE_AND_STATUS_FOLIO),
        UPDATE_ITEMS(5, ApplicationConstant.TASK_UPDATE_ITEMS_FOLIO),
        GENERAL_DATA_UPDATED(6, ApplicationConstant.TASK_GENERAL_DATA_UPDATED);

        private final Integer id;
        private final String description;

        StatusTask(Integer id, String description) {
            this.id = id;
            this.description = description;
        }

        // Método de búsqueda moderno usando Pattern Matching (Java 21)
        public static StatusTask fromId(Long id) {
            for (StatusTask action : values()) {
                if (action.id.equals(id)) return action;
            }
            throw new IllegalArgumentException(STR."Action ID no válido: \{id}");
        }
    }

    // Único punto de entrada permitido para el exterior
    public final void executeTaskWorkflow(
            EventDTO currentEvent,
            EventDTO eventToUpdate,
            List<DetailRentaDTO> detailToUpdate,
            List<DetailRentaDTO> currentDetail,
            UserDTO userSession) throws BusinessException {

        // 1. Logs o pre-validaciones globales (Dry Run)
        log.info(LogConstant.INIT_WORK_FLOW_TASK, currentEvent.folio());

        // 2. Llamada al método protegido que solo las hijas conocen
       process(currentEvent, eventToUpdate, detailToUpdate,
               currentDetail, userSession);

        // 3. Post-procesamiento o auditoría global
        log.info(LogConstant.END_WORK_FLOW_TASK, currentEvent.folio());

    }

    protected abstract void process (
            final EventDTO currentEvent,
            final EventDTO eventToUpdate,
            final List<DetailRentaDTO> detailToUpdate,
            final List<DetailRentaDTO> currentDetail,
            UserDTO userSession) throws BusinessException;

}
