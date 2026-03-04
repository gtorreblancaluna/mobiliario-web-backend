package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.AlmacenTaskRepository;
import com.mx.gaby.mobiliario_web.repositories.ChoferDeliveryTaskRepository;
import com.mx.gaby.mobiliario_web.services.MessageStorageService;
import com.mx.gaby.mobiliario_web.services.TaskWarehouseService;
import com.mx.gaby.mobiliario_web.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Log4j2
public class TaskWarehouseDeliveryDriverSaveServiceImpl
        extends TaskWarehouseService {

    private static final String TASK_CONTEXT_NAME
            = ApplicationConstant.TASK_CONTEXT_NAME_SAVE_DELIVERY_DRIVERS;

    protected TaskWarehouseDeliveryDriverSaveServiceImpl(
            MessageStorageService messageStorageService,
            UserService userService,
            ChoferDeliveryTaskRepository choferDeliveryTaskRepository,
            AlmacenTaskRepository almacenTaskRepository) {
        super(TASK_CONTEXT_NAME,messageStorageService, userService, choferDeliveryTaskRepository, almacenTaskRepository);
    }

    @Override
    protected void process(
            EventDTO currentEvent,
            EventDTO eventToUpdate,
            List<DetailRentaDTO> detailToUpdate,
            List<DetailRentaDTO> currentDetail,
            UserDTO userSession) throws BusinessException {

        generateTaskToDeliveryDriver(currentEvent, StatusTask.NEW, userSession);

    }
}
