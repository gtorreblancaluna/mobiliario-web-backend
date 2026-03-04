package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.Event;
import com.mx.gaby.mobiliario_web.model.entitites.User;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.EventDetailDTO;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.DetailEventRepository;
import com.mx.gaby.mobiliario_web.repositories.EventRepository;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.repositories.UserRepository;
import com.mx.gaby.mobiliario_web.services.EventService;
import com.mx.gaby.mobiliario_web.services.EventTaskOrchestrator;
import com.mx.gaby.mobiliario_web.services.MessageStorageService;
import com.mx.gaby.mobiliario_web.services.UserService;
import com.mx.gaby.mobiliario_web.utils.DateUtil;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.time.LocalDate;


@Service
@Log4j2
@Transactional
public class EventSaveServiceImpl extends EventService {

    private final MessageStorageService messageStorageService;
    private final EventTaskOrchestrator taskOrchestrator;

    protected EventSaveServiceImpl(
            EventRepository eventRepository,
            DetailEventRepository detailEventRepository,
            PaymentRepository paymentRepository,
            UserService userService,
            UserRepository userRepository,
            MessageStorageService messageStorageService,
            EventTaskOrchestrator taskOrchestrator) {
        super(eventRepository, detailEventRepository, paymentRepository, userService, userRepository);
        this.messageStorageService = messageStorageService;
        this.taskOrchestrator = taskOrchestrator;
    }

    private Integer getAndPlusFolio () throws BusinessException {

        // para obtener el folio
        Integer currentFolio
                = eventRepository.findLastFolio();

        return (currentFolio + 1);
    }

    @Override
    protected Event save(final EventDetailDTO eventDetailDTO) {

        UserDTO userSession = userService.getAuthenticatedUser();

        Event eventToSaveEntity
                = EventDTO.fromDTO(eventDetailDTO.event());

        User user = new User();
        user.setId(userSession.id());
        eventToSaveEntity.setUser(user);

        eventToSaveEntity.setFolio(getAndPlusFolio());
        eventToSaveEntity.setId(null);

        eventToSaveEntity
                .setFechaPedido(
                        DateUtil.getStringTodayDateFormatDatabase());

        Event eventSaved = eventRepository.save(eventToSaveEntity);

        String logMessage =
                MessageFormat.format(
                        LogConstant.EVENT_SAVED_SUCCESSFULLY, eventSaved.getFolio(),
                        userSession.fullName());

        log.info(logMessage);

        messageStorageService.addMessage(logMessage);

        generateTasks(null,EventDTO.fromEntity(eventSaved));

        return eventSaved;
    }

    @Override
    protected void generateTasks(
            EventDetailDTO eventDetailDTO,
            EventDTO currentEventDTO) throws BusinessException {

        UserDTO userSession = userService.getAuthenticatedUser();

        taskOrchestrator.handleTasks(
                currentEventDTO,
                null,
                null, null,
                userSession, false);

    }

 }
