package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.MessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.*;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.ChoferDeliveryTaskRepository;
import com.mx.gaby.mobiliario_web.services.MessageStorageService;
import com.mx.gaby.mobiliario_web.services.TaskWarehouseService;
import com.mx.gaby.mobiliario_web.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@Log4j2
public class TaskWarehouseManagerUpdateServiceImpl extends TaskWarehouseService {

    private final UserService userService;
    private final ChoferDeliveryTaskRepository choferDeliveryTaskRepository;
    private final MessageStorageService messageStorageService;

    public TaskWarehouseManagerUpdateServiceImpl(
            UserService userService,
            ChoferDeliveryTaskRepository choferDeliveryTaskRepository, MessageStorageService messageStorageService) {
        super(messageStorageService);
        this.userService = userService;
        this.choferDeliveryTaskRepository = choferDeliveryTaskRepository;
        this.messageStorageService = messageStorageService;
    }

    private List<UserDTO> getWarehouseManagers()
            throws BusinessException {

        List<UserDTO> users
                = userService.getWarehouseManagers();

        if (users.isEmpty()) {
            throw new BusinessException(MessageConstant.TASK_WAREHOUSE_NO_USERS_FOUND);
        }

        return users;
    }


    private void generateTaskWarehouseManagers(
            final EventDTO eventToUpdate,
            final StatusTask statusTask,
            final UserDTO userSession) throws BusinessException{

        List<UserDTO> warehouseManagers = getWarehouseManagers();

        for (UserDTO warehouseManager : warehouseManagers) {

            DeliveryDriverTask deliveryDriverTask
                    = getChoferDeliveryTask(eventToUpdate,statusTask,warehouseManager.id());

            deliveryDriverTask.setCreatedBy(userSession.id());

            choferDeliveryTaskRepository.save(deliveryDriverTask);

            String logMessage = MessageFormat.format(LogConstant.MESSAGE_GENERATE_TASK_WAREHGOUSE_MANAGER,
                    warehouseManager.name(),eventToUpdate.folio());

            messageStorageService.addMessage(logMessage);

            log.info(logMessage);

        }

    }

    @Override
    protected void process(
           EventDTO currentEvent,
           EventDTO eventToUpdate,
           List<DetailRentaDTO> detailToUpdate,
           List<DetailRentaDTO> currentDetail,
           final UserDTO userSession) throws BusinessException {

        StatusTask statusTask = applyRulesAndGetStatus (
                currentEvent,eventToUpdate,detailToUpdate,currentDetail);

        generateTaskWarehouseManagers(eventToUpdate, statusTask, userSession);

    }
}
