package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.services.impl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@RequiredArgsConstructor
public class EventTaskOrchestrator {

    // Inyectamos todas las implementaciones necesarias
    private final TaskWarehouseManagerSaveServiceImpl warehouseManagerSaveService;
    private final TaskWarehouseManagerUpdateServiceImpl warehouseManagerUpdateService;
    private final TaskUsersInCategoriesUpdateServiceImpl usersInCategoriesUpdateService;
    private final TaskUsersInCategoriesSaveServiceImpl usersInCategoriesSaveService;
    private final TaskWarehouseDeliveryDriverUpdateServiceImpl deliveryDriverUpdateService;
    private final TaskWarehouseDeliveryDriverSaveServiceImpl deliveryDriverSaveService;
    private final MessageStorageService messageStorageService;


    private boolean validateTypeAndStatus(final EventDTO event) {
        String typeId = event.tipoId().toString();
        String statusId = event.estadoId().toString();

        boolean isValid = true;

        // Las cotizaciones NO generan tareas de logística/almacén
        if (typeId.equals(ApplicationConstant.TIPO_COTIZACION)) {

            isValid = false;
            String message;

            // Caso A: Es cotización pero intentan ponerle un estado que no le corresponde (Ej: Apartado)
            if (!statusId.equals(ApplicationConstant.ESTADO_PENDIENTE)) {

                message = MessageFormat.format(
                        ValidationMessageConstant.NO_TASKS_GENERATED_BY_TYPE_AND_STATUS,
                        event.folio(),
                        ApplicationConstant.DS_TIPO_COTIZACION,
                        ApplicationConstant.DS_ESTADO_PENDIENTE);
            } else {
                // Caso B: Es cotización pendiente (Lo cual es correcto, pero no genera tareas)
                // Lanzamos una excepción específica que el Orquestador debe atrapar
                // para detener la generación de tareas sin marcarlo como un "error fatal".
                message = MessageFormat.format(
                        ValidationMessageConstant.NO_TASKS_GENERATED_BY_COTIZACION_AND_PENDING,
                        event.folio(), ApplicationConstant.DS_TIPO_COTIZACION);

            }

            if (!message.isBlank()) {
                messageStorageService.addMessage(message);
            }

        }

        return isValid;
    }


    public void handleTasks(
            EventDTO currentEvent,
            EventDTO eventToUpdate,
            List<DetailRentaDTO> detailToUpdate,
            List<DetailRentaDTO> currentDetail,
            UserDTO userSession,
            boolean isUpdate) {

        // 1. Validación inicial (Fail Fast)
        EventDTO targetEvent = isUpdate ? eventToUpdate : currentEvent;

        if (!validateTypeAndStatus(targetEvent)) {
            return;
        }

        String initWorkFlowMessage =
                MessageFormat.format(LogConstant.INIT_WORK_FLOW_TASK,targetEvent.folio());

        messageStorageService.addMessage(initWorkFlowMessage);
        log.info(initWorkFlowMessage);

        // 2. Definir qué servicios ejecutar según el tipo de operación
        List<TaskWarehouseService> servicesToExecute = isUpdate
                ? List.of(usersInCategoriesUpdateService, deliveryDriverUpdateService, warehouseManagerUpdateService)
                : List.of(usersInCategoriesSaveService, warehouseManagerSaveService, deliveryDriverSaveService);

        // 3. Ejecución Asíncrona Orquestada
        List<CompletableFuture<Void>> futures = servicesToExecute.stream()
                .map(service -> CompletableFuture.runAsync(() -> {
                    try {
                        // Para Save: enviamos el evento en la posición de 'eventToUpdate' y nulos en el resto
                        if (isUpdate) {
                            service.executeTaskWorkflow(currentEvent, eventToUpdate, detailToUpdate, currentDetail, userSession);
                        } else {
                            service.executeTaskWorkflow(currentEvent, null, null, null, userSession);
                        }
                    } catch (BusinessException e) {
                        log.error(e.getMessage(),e);
                        messageStorageService.addMessage(e.getMessage());
                    } catch (Exception e) {
                        String errorMsg =
                                MessageFormat.format(LogConstant.ERROR_TASK_EXECUTOR,
                                        service.taskContextName, e.getMessage());
                        log.error(errorMsg);
                        messageStorageService.addMessage(errorMsg);
                    }
                }))
                .toList();

        // 4. Acción final cuando todos terminen
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> {
                    String msg = MessageFormat.format(LogConstant.MSG_ALL_WORKFLOWS_PROCESSED, targetEvent.folio());
                    messageStorageService.addMessage(msg);
                    log.info(msg);
                });
    }
}