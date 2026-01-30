package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.Event;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.RentaDetailDTO;
import com.mx.gaby.mobiliario_web.repositories.DetailEventRepository;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.repositories.EventRepository;
import com.mx.gaby.mobiliario_web.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional
public class EventUpdateServiceImpl extends EventService {


    private final TaskAlmacenUpdateServiceImpl taskAlmacenUpdateService;
    private final TaskAlmacenChoferUpdateServiceImpl taskAlmacenChoferUpdateService;

    public EventUpdateServiceImpl(

            DetailEventRepository detailEventRepository,
            PaymentRepository paymentRepository,
            TaskAlmacenUpdateServiceImpl taskAlmacenUpdateService,
            TaskAlmacenChoferUpdateServiceImpl taskAlmacenChoferUpdateService,
            UserService userService,
            UserRepository userRepository,
            EventRepository eventRepository, MessageStorageService messageStorageService) {

        super(eventRepository,
                detailEventRepository,
                paymentRepository, userService, userRepository, messageStorageService); // El padre recibe su dependencia

        this.taskAlmacenUpdateService = taskAlmacenUpdateService;
        this.taskAlmacenChoferUpdateService = taskAlmacenChoferUpdateService;

    }


    @Override
    protected String save(RentaDetailDTO rentaDetailDTO) {

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

        log.info(LogConstant.EVENT_UPDATED_SUCCESSFULLY, rentaDetailDTO.event().folio());

        EventDTO currentEventDTO
                = EventDTO.fromEntity(currentEventOptional.get());

        String messageGeneratedTasks = generateTasks(rentaDetailDTO,currentEventDTO);

        return "Se ha guardado con éxito el evento, folio: %s. %s %s "
                .formatted(rentaDetailDTO.event().folio(),
                        ApplicationConstant.BREAK_LINE_HTML,
                        messageGeneratedTasks);
        
    }

    @Override
    protected String generateTasks (
            final RentaDetailDTO rentaDetailDTO,
            final EventDTO currentEventDTO)
            throws BusinessException {

        List<DetailRentaDTO> currentDetailEvent
                = detailEventRepository
                .findByEventId(currentEventDTO.id())
                .stream()
                .map(DetailRentaDTO::fromEntity)
                .toList();

        StringBuilder stringBuilder = new StringBuilder();

        EventDTO eventToUpdate = rentaDetailDTO.event();
        List<DetailRentaDTO> detailToUpdate = rentaDetailDTO.detail();

        try {
                stringBuilder.append(
                    taskAlmacenUpdateService.executeTaskWorkflow(
                        currentEventDTO,
                        eventToUpdate,
                        detailToUpdate,
                        currentDetailEvent));

                stringBuilder.append(ApplicationConstant.BREAK_LINE_HTML);


        } catch (BusinessException businessException) {

            stringBuilder.append(businessException.getMessage());
            stringBuilder.append(ApplicationConstant.BREAK_LINE_HTML);
        }

        try {

            stringBuilder.append(
                taskAlmacenChoferUpdateService.executeTaskWorkflow(
                    currentEventDTO,
                    eventToUpdate,
                    detailToUpdate,
                    currentDetailEvent));
            stringBuilder.append(ApplicationConstant.BREAK_LINE_HTML);

        } catch (BusinessException businessException) {
            stringBuilder.append(businessException.getMessage());
            stringBuilder.append(ApplicationConstant.BREAK_LINE_HTML);
        }

        return stringBuilder.toString();
    }

}
