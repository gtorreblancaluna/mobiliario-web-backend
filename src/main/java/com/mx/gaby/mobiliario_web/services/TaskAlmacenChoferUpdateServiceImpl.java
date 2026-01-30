package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.MessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.*;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.ChoferDeliveryTaskRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@Log4j2
public class TaskAlmacenChoferUpdateServiceImpl extends TaskAlmacenService{

    private final UserService userService;
    private final ChoferDeliveryTaskRepository choferDeliveryTaskRepository;

    public TaskAlmacenChoferUpdateServiceImpl(
            UserService userService,
            ChoferDeliveryTaskRepository choferDeliveryTaskRepository) {
        this.userService = userService;
        this.choferDeliveryTaskRepository = choferDeliveryTaskRepository;
    }

    private List<UserDTO> getEncargadosDeAlmacenUsers ()
            throws BusinessException {

        List<UserDTO> users = userService.getEncargadosDeAlmacenUsers();

        if (users.isEmpty()) {
            throw new BusinessException("""
                    No se generaron tareas
                    para los encargados de almacen
                    por que no se obtuvieron usuarios de la base de datos.""");
        }

        return users;
    }

    private ChoferDeliveryTask getChoferDeliveryTask (
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

    private String generateTaskToChofer (
            EventDTO eventToUpdate,
            StatusTask statusTask) {

        ChoferDeliveryTask choferDeliveryTask
                = getChoferDeliveryTask(eventToUpdate,statusTask,eventToUpdate.choferId());

        choferDeliveryTaskRepository.save(choferDeliveryTask);

        log.info(LogConstant.MESSAGE_GENERATE_TASK_CHOFER,
                eventToUpdate.choferName(),
                eventToUpdate.folio());

        return MessageFormat.format(MessageConstant.MESSAGE_GENERATE_TASK_CHOFER,eventToUpdate.choferName());

    }

    private String generateTaskWarehouseManagers(
            EventDTO eventToUpdate,
            StatusTask statusTask) throws BusinessException{

        List<UserDTO> warehouseManagers = getEncargadosDeAlmacenUsers();

        StringBuilder sb = new StringBuilder();

        for (UserDTO warehouseManager : warehouseManagers) {

            ChoferDeliveryTask choferDeliveryTask
                    = getChoferDeliveryTask(eventToUpdate,statusTask,warehouseManager.id());

            choferDeliveryTaskRepository.save(choferDeliveryTask);

                sb.append(
                        MessageFormat.format(
                                MessageConstant.MESSAGE_GENERATE_TASK_ENCARGADO_ALMACEN,
                                warehouseManager.name()
                        ));
                sb.append(ApplicationConstant.BREAK_LINE_HTML);

                log.info(LogConstant.MESSAGE_GENERATE_TASK_ENCARGADO_ALMACEN,
                    warehouseManager.name(),eventToUpdate.folio()
            );

        }

        return sb.toString();

    }

    @Override
    protected String process(
           EventDTO currentEvent,
           EventDTO eventToUpdate,
           List<DetailRentaDTO> detailToUpdate,
           List<DetailRentaDTO> currentDetail) throws BusinessException {

        StatusTask statusTask = applyRulesAndGetStatus (
                currentEvent,eventToUpdate,detailToUpdate,currentDetail);


        String logsTaskChofer =
                ApplicationConstant.LINE_HTML +
                ApplicationConstant.BREAK_LINE_HTML +
                MessageConstant.TASK_FOR_CHOFER +
                ApplicationConstant.BREAK_LINE_HTML +
                ApplicationConstant.LINE_HTML +
                ApplicationConstant.BREAK_LINE_HTML +
                generateTaskToChofer(eventToUpdate, statusTask);

        StringBuilder logsTaskWarehouseManager = new StringBuilder();
        logsTaskWarehouseManager
                .append(ApplicationConstant.LINE_HTML)
                .append(ApplicationConstant.BREAK_LINE_HTML )
                .append(MessageConstant.TASK_FOR_WAREHOUSE_MANAGER)
                .append(ApplicationConstant.BREAK_LINE_HTML)
                .append(ApplicationConstant.LINE_HTML)
                .append(ApplicationConstant.BREAK_LINE_HTML );

        try {
            logsTaskWarehouseManager
                    .append(generateTaskWarehouseManagers(eventToUpdate, statusTask));
        } catch (BusinessException businessException) {
            logsTaskWarehouseManager
                    .append(businessException.getMessage());
        }

        return logsTaskWarehouseManager +
                ApplicationConstant.BREAK_LINE_HTML +
                ApplicationConstant.LINE_HTML +
                logsTaskChofer;

    }
}
