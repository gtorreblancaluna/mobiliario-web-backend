package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.*;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.AlmacenTaskRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;


@Service
@Log4j2
public class TaskAlmacenUpdateServiceImpl extends TaskAlmacenService{

    private final AlmacenTaskRepository almacenTaskRepository;
    private final UserService userService;
    private final MessageStorageService messageStorageService;

    public TaskAlmacenUpdateServiceImpl(AlmacenTaskRepository almacenTaskRepository, UserService userService, MessageStorageService messageStorageService) {
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
            AlmacenTask almacenTask = getAlmacenTask(currentEvent, statusTask,userDTO);

            almacenTask.setCreatedBy(userSession.id());

            almacenTaskRepository.save(almacenTask);

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
                userService.getUsersInCategoriesAlmacenAndEvent(currentEvent.id());

        validateUsers(usersInCategories, currentEvent.folio());

        // 2. Cálculo de reglas (Heredado de la clase padre)
        StatusTask statusTask = applyRulesAndGetStatus (
                currentEvent,eventToUpdate,detailToUpdate,currentDetail);

       saveTasksForUsers(usersInCategories, currentEvent, statusTask, userSession);

    }

    private static AlmacenTask getAlmacenTask(
            EventDTO currentEvent, StatusTask statusTask, UserDTO userDTO) {

        AlmacenTask almacenTask = new AlmacenTask();

        // Event
        Event event = new Event();
        event.setId(currentEvent.id());
        almacenTask.setEvent(event);

        // status
        AlmacenTaskStatus almacenTaskStatus = new AlmacenTaskStatus();
        almacenTaskStatus.setId(statusTask.getId());
        almacenTask.setStatus(almacenTaskStatus);

        // type
        AttendAlmacenTaskType attendAlmacenTaskType = new AttendAlmacenTaskType();
        attendAlmacenTaskType.setId(1);
        almacenTask.setType(attendAlmacenTaskType);

        // User
        User userByCategory = new User();
        userByCategory.setId(userDTO.id());
        almacenTask.setUserByCategory(userByCategory);

        almacenTask.setFgActive(true);

        return almacenTask;
    }

}
