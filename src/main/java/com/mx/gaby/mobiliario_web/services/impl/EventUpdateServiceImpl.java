package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.Event;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.EventDetailDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.DetailEventRepository;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.repositories.EventRepository;
import com.mx.gaby.mobiliario_web.repositories.UserRepository;
import com.mx.gaby.mobiliario_web.services.EventService;
import com.mx.gaby.mobiliario_web.services.EventTaskOrchestrator;
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

@Service
@Log4j2
@Transactional
public class EventUpdateServiceImpl extends EventService {

    private final MessageStorageService messageStorageService;
    private final EventTaskOrchestrator taskOrchestrator;

    public EventUpdateServiceImpl(
            DetailEventRepository detailEventRepository,
            PaymentRepository paymentRepository,
            UserService userService,
            UserRepository userRepository,
            EventRepository eventRepository,
            MessageStorageService messageStorageService,
            EventTaskOrchestrator taskOrchestrator) {

        super(eventRepository,
                detailEventRepository,
                paymentRepository,
                userService, userRepository);

        this.messageStorageService = messageStorageService;
        this.taskOrchestrator = taskOrchestrator;
    }


    @Override
    protected Event save(final EventDetailDTO eventDetailDTO) {

        Event eventToUpdateEntity
                = EventDTO.fromDTO(eventDetailDTO.event());

        // necessary for generate task.
        @NonNull Optional<Event> currentEventOptional
                = eventRepository.findById(eventDetailDTO.event().id());

        if (currentEventOptional.isEmpty()) {
            throw new BusinessException(
                    ValidationMessageConstant.ERROR_EVENT_NOT_FOUND_WHEN_TRYING_GENERATE_TASK);
        }

        eventToUpdateEntity.setUpdatedAt(Timestamp.from(Instant.now()));
        eventRepository.updateEvent(eventToUpdateEntity);

        UserDTO userSession = userService.getAuthenticatedUser();

        String logMessage =
                MessageFormat.format(
                        LogConstant.EVENT_UPDATED_SUCCESSFULLY, eventDetailDTO.event().folio(),
                        userSession.fullName());

        log.info(logMessage);

        messageStorageService.addMessage(logMessage);

        EventDTO currentEventDTO
                = EventDTO.fromEntity(currentEventOptional.get());

        generateTasks(eventDetailDTO,currentEventDTO);

        return eventToUpdateEntity;

    }

    @Override
    protected void generateTasks (
            final EventDetailDTO eventDetailDTO,
            final EventDTO currentEventDTO)
                throws BusinessException {

        List<DetailRentaDTO> currentDetailEvent
                = detailEventRepository
                .findByEventId(currentEventDTO.id())
                .stream()
                .map(DetailRentaDTO::fromEntity)
                .toList();


        EventDTO eventToUpdate = eventDetailDTO.event();
        List<DetailRentaDTO> detailToUpdate = eventDetailDTO.detail();
        UserDTO userSession = userService.getAuthenticatedUser();

        taskOrchestrator.handleTasks(
                currentEventDTO,
                eventToUpdate,
                detailToUpdate,
                currentDetailEvent,
                userSession, true);

    }

}
