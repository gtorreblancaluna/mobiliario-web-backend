package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.Event;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.RentaDetailDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.DetailEventRepository;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.repositories.EventRepository;
import com.mx.gaby.mobiliario_web.repositories.UserRepository;
import com.mx.gaby.mobiliario_web.services.EventService;
import com.mx.gaby.mobiliario_web.services.MessageStorageService;
import com.mx.gaby.mobiliario_web.services.UserService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Log4j2
@Transactional
public class EventUpdateServiceImpl extends EventService {

    private final TaskWarehouseUpdateServiceImpl taskWarehouseUpdateService;
    private final TaskWarehouseChoferUpdateServiceImpl taskDriverManUpdateService;
    private final TaskWarehouseManagerUpdateServiceImpl taskWarehouseManagerUpdateService;
    private final MessageStorageService messageStorageService;

    public EventUpdateServiceImpl(

            DetailEventRepository detailEventRepository,
            PaymentRepository paymentRepository,
            TaskWarehouseUpdateServiceImpl taskWarehouseUpdateService,
            TaskWarehouseChoferUpdateServiceImpl taskDriverManUpdateService,
            UserService userService,
            UserRepository userRepository,
            EventRepository eventRepository,
            TaskWarehouseManagerUpdateServiceImpl taskWarehouseManagerUpdateService,
            MessageStorageService messageStorageService) {

        super(eventRepository,
                detailEventRepository,
                paymentRepository, userService, userRepository); // El padre recibe su dependencia
        this.taskWarehouseUpdateService = taskWarehouseUpdateService;
        this.taskDriverManUpdateService = taskDriverManUpdateService;
        this.taskWarehouseManagerUpdateService = taskWarehouseManagerUpdateService;
        this.messageStorageService = messageStorageService;
    }


    @Override
    protected void save(RentaDetailDTO rentaDetailDTO) {

        Event eventToUpdateEntity
                = EventDTO.fromDTO(rentaDetailDTO.event());

        // necessary for generate task.
        @NonNull Optional<Event> currentEventOptional
                = eventRepository.findById(rentaDetailDTO.event().id());

        if (currentEventOptional.isEmpty()) {
            throw new BusinessException(
                    ValidationMessageConstant.ERROR_EVENT_NOT_FOUND_WHEN_TRYING_GENERATE_TASK);
        }

        eventToUpdateEntity.setUpdatedAt(Timestamp.from(Instant.now()));
        eventRepository.updateEvent(eventToUpdateEntity);

        UserDTO userSession = userService.getAuthenticatedUser();

        String logMessage =
                MessageFormat.format(
                        LogConstant.EVENT_UPDATED_SUCCESSFULLY, rentaDetailDTO.event().folio(),
                        userSession.fullName());

        log.info(logMessage);

        messageStorageService.addMessage(logMessage);

        EventDTO currentEventDTO
                = EventDTO.fromEntity(currentEventOptional.get());

        generateTasks(rentaDetailDTO,currentEventDTO);

    }

    @Override
    protected void generateTasks (
            final RentaDetailDTO rentaDetailDTO,
            final EventDTO currentEventDTO)
                throws BusinessException {

        List<DetailRentaDTO> currentDetailEvent
                = detailEventRepository
                .findByEventId(currentEventDTO.id())
                .stream()
                .map(DetailRentaDTO::fromEntity)
                .toList();


        EventDTO eventToUpdate = rentaDetailDTO.event();
        List<DetailRentaDTO> detailToUpdate = rentaDetailDTO.detail();
        UserDTO userSession = userService.getAuthenticatedUser();

        CompletableFuture<Void> taskWarehouse = CompletableFuture.runAsync(() -> {
            try {
                taskWarehouseUpdateService
                        .executeTaskWorkflow(
                                currentEventDTO,
                                eventToUpdate,
                                detailToUpdate,
                                currentDetailEvent, userSession);
            } catch (Exception e) {
                messageStorageService.addMessage(e.getMessage());
            }
        });

        CompletableFuture<Void> taskDriverMan
                = CompletableFuture.runAsync(() -> {
            try {
                taskDriverManUpdateService
                        .executeTaskWorkflow(
                                currentEventDTO,
                                eventToUpdate,
                                detailToUpdate,
                                currentDetailEvent,userSession);
            } catch (Exception e) {
                messageStorageService.addMessage(e.getMessage());
            }
        });

        CompletableFuture<Void> taskWarehouseManager
                = CompletableFuture.runAsync(() -> {
            try {
                taskWarehouseManagerUpdateService
                        .executeTaskWorkflow(
                                currentEventDTO,
                                eventToUpdate,
                                detailToUpdate,
                                currentDetailEvent,userSession);
            } catch (Exception e) {
                messageStorageService.addMessage(e.getMessage());
            }
        });

        CompletableFuture.allOf(taskWarehouse, taskDriverMan, taskWarehouseManager)
                .thenRun(() -> messageStorageService.addMessage(
                        LogConstant.MSG_ALL_WORKFLOWS_PROCESSED));

    }

}
