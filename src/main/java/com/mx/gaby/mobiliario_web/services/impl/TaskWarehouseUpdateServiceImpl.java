package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.*;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.AlmacenTaskRepository;
import com.mx.gaby.mobiliario_web.services.MessageStorageService;
import com.mx.gaby.mobiliario_web.services.TaskWarehouseService;
import com.mx.gaby.mobiliario_web.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;


@Service
@Log4j2
public class TaskWarehouseUpdateServiceImpl extends TaskWarehouseService {

    private final AlmacenTaskRepository almacenTaskRepository;
    private final UserService userService;
    private final MessageStorageService messageStorageService;

    public TaskWarehouseUpdateServiceImpl(
            AlmacenTaskRepository almacenTaskRepository,
            UserService userService, MessageStorageService messageStorageService) {
        super(messageStorageService);
        this.almacenTaskRepository = almacenTaskRepository;
        this.userService = userService;
        this.messageStorageService = messageStorageService;
    }

    private void validateUsers(List<UserDTO> users, Integer folio)
            throws BusinessException {
        if (users == null || users.isEmpty()) {
            throw new BusinessException(MessageFormat.format(
                    ValidationMessageConstant.NO_GENERATE_TASK_USERS_IN_CATEGORIES_NOT_FOUND, folio));
        }
    }

    private void saveTasksForUsers (
            List<UserDTO> usersInCategories,
            final EventDTO currentEvent,
            StatusTask statusTask,
            final UserDTO userSession) {

        for (UserDTO userDTO : usersInCategories) {
            WarehouseTask warehouseTask = getAlmacenTask(currentEvent, statusTask,userDTO);

            warehouseTask.setCreatedBy(userSession.id());

            almacenTaskRepository.save(warehouseTask);

           String logMessage = MessageFormat.format(
                   LogConstant.TASK_ALMACEN_SUCCESSFULY_CREATED,userDTO.name(),currentEvent.folio());

            messageStorageService.addMessage(logMessage);
            log.info(logMessage);
        }

    }

    @Override
    protected void process(
            final EventDTO currentEvent,
            final EventDTO eventToUpdate,
            final List<DetailRentaDTO> detailToUpdate,
            final List<DetailRentaDTO> currentDetail,
            final UserDTO userSession
            ) throws BusinessException {

        // 1. Obtención de responsables (Lógica específica)
        List<UserDTO> usersInCategories =
                userService.getUsersInCategoriesWarehouseAndEvent(currentEvent.id());

        validateUsers(usersInCategories, currentEvent.folio());

        // 2. Cálculo de reglas (Heredado de la clase padre)
        StatusTask statusTask = applyRulesAndGetStatus (
                currentEvent,eventToUpdate,detailToUpdate,currentDetail);

       saveTasksForUsers(usersInCategories, currentEvent, statusTask, userSession);

    }

    private static WarehouseTask getAlmacenTask(
            EventDTO currentEvent, StatusTask statusTask, UserDTO userDTO) {

        WarehouseTask warehouseTask = new WarehouseTask();

        // Event
        Event event = new Event();
        event.setId(currentEvent.id());
        warehouseTask.setEvent(event);

        // status
        WarehouseTaskStatus warehouseTaskStatus = new WarehouseTaskStatus();
        warehouseTaskStatus.setId(statusTask.getId());
        warehouseTask.setStatus(warehouseTaskStatus);

        // type
        AttendWarehouseTaskType attendWarehouseTaskType = new AttendWarehouseTaskType();
        attendWarehouseTaskType.setId(1);
        warehouseTask.setType(attendWarehouseTaskType);

        // User
        User userByCategory = new User();
        userByCategory.setId(userDTO.id());
        warehouseTask.setUserByCategory(userByCategory);

        warehouseTask.setFgActive(true);

        return warehouseTask;
    }

}
