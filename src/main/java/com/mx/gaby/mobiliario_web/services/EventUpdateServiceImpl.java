package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.Event;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.records.RentaDetailDTO;
import com.mx.gaby.mobiliario_web.repositories.DetailRentaRepository;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.repositories.EventRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

@Service
@Log4j2
@Transactional
public class EventUpdateServiceImpl extends EventService {

    private final EventRepository eventRepository;

    public EventUpdateServiceImpl(EventRepository eventRepository,
                                  DetailRentaRepository detailRentaRepository,
                                  PaymentRepository paymentRepository) {
        super(detailRentaRepository,paymentRepository); // El padre recibe su dependencia
        this.eventRepository = eventRepository;
    }

    @Override
    protected void save(RentaDetailDTO rentaDetailDTO)
            throws BusinessException {

        /*
        @NonNull Optional<Event> OpCurrentRenta
                = rentaRepository.findById(rentaDetailDTO.event().id());

         */


        Event eventToUpdateEntity
                = EventDTO.fromDTO(rentaDetailDTO.event());

        eventToUpdateEntity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        eventRepository.updateEvent(eventToUpdateEntity);

        log.info(LogConstant.RENTA_UPDATED_SUCCESSFULLY,rentaDetailDTO.event().folio());

        saveDetails(rentaDetailDTO);

        savePayments(rentaDetailDTO);
        
    }

}
