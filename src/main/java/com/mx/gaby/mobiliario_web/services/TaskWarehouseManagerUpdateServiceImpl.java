package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
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
public class TaskWarehouseManagerUpdateServiceImpl extends TaskAlmacenService{

    private final UserService userService;
    private final ChoferDeliveryTaskRepository choferDeliveryTaskRepository;
    private final MessageStorageService messageStorageService;

    public TaskWarehouseManagerUpdateServiceImpl(
            UserService userService,
            ChoferDeliveryTaskRepository choferDeliveryTaskRepository, MessageStorageService messageStorageService) {
        this.userService = userService;
        this.choferDeliveryTaskRepository = choferDeliveryTaskRepository;
        this.messageStorageService = messageStorageService;
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


    private void generateTaskWarehouseManagers(
            EventDTO eventToUpdate,
            StatusTask statusTask,
            final UserDTO userSession) throws BusinessException{

        List<UserDTO> warehouseManagers = getEncargadosDeAlmacenUsers();

        for (UserDTO warehouseManager : warehouseManagers) {

            ChoferDeliveryTask choferDeliveryTask
                    = getChoferDeliveryTask(eventToUpdate,statusTask,warehouseManager.id());

            choferDeliveryTask.setCreatedBy(userSession.id());

            choferDeliveryTaskRepository.save(choferDeliveryTask);

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
