package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.*;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.ChoferDeliveryTaskRepository;
import com.mx.gaby.mobiliario_web.services.MessageStorageService;
import com.mx.gaby.mobiliario_web.services.TaskWarehouseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@Log4j2
public class TaskWarehouseDeliveryDriverUpdateServiceImpl
        extends TaskWarehouseService {

    private final MessageStorageService messageStorageService;
    private final ChoferDeliveryTaskRepository choferDeliveryTaskRepository;

    public TaskWarehouseDeliveryDriverUpdateServiceImpl(
            MessageStorageService messageStorageService, ChoferDeliveryTaskRepository choferDeliveryTaskRepository) {
        super(messageStorageService);
        this.messageStorageService = messageStorageService;
        this.choferDeliveryTaskRepository = choferDeliveryTaskRepository;
    }


    private void generateTaskToDeliveryDriver(
            final EventDTO eventToUpdate,
            final StatusTask statusTask,
            final UserDTO userSession) {

        DeliveryDriverTask deliveryDriverTask
                = getChoferDeliveryTask(eventToUpdate,statusTask,eventToUpdate.choferId());

        deliveryDriverTask.setCreatedBy(userSession.id());

        choferDeliveryTaskRepository.save(deliveryDriverTask);

        String logMessage = MessageFormat.format(
                LogConstant.MESSAGE_GENERATE_TASK_CHOFER,
                eventToUpdate.choferName(),
                eventToUpdate.folio());

        log.info(logMessage);

        messageStorageService.addMessage(logMessage);


    }

    @Override
    protected void process(
            EventDTO currentEvent,
            EventDTO eventToUpdate,
            List<DetailRentaDTO> detailToUpdate,
            List<DetailRentaDTO> currentDetail,
            UserDTO userSession) throws BusinessException {

        StatusTask statusTask = applyRulesAndGetStatus (
                currentEvent,eventToUpdate,detailToUpdate,currentDetail);

         generateTaskToDeliveryDriver(eventToUpdate, statusTask, userSession);

    }
}
