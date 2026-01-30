package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.exceptions.NotFoundException;
import com.mx.gaby.mobiliario_web.model.entitites.Payment;
import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;
import com.mx.gaby.mobiliario_web.model.entitites.Event;
import com.mx.gaby.mobiliario_web.records.*;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.repositories.DetailEventRepository;
import com.mx.gaby.mobiliario_web.repositories.EventRepository;
import com.mx.gaby.mobiliario_web.repositories.specification.RentaSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventQueryServiceImpl implements EventQueryService {

    private final EventRepository eventRepository;
    private final DetailEventRepository detailEventRepository;
    private final PaymentRepository paymentRepository;

    public EventQueryServiceImpl(EventRepository eventRepository, DetailEventRepository detailEventRepository, PaymentRepository paymentRepository) {
        this.eventRepository = eventRepository;
        this.detailEventRepository = detailEventRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public RentaDetailDTO findById(Integer id) {

        Event eventEntity = eventRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Renta con ID " + id + " no encontrada."));

        List<DetailRenta> detailEntities
                = detailEventRepository.findByEventId(eventEntity.getId());

        List<Payment> abonosEntities =
                paymentRepository.findByRentaId(eventEntity.getId());

        List<PaymentDTO> abonos =
                abonosEntities.stream()
                .map(PaymentDTO::fromEntity)
                .toList();

        List<DetailRentaDTO> detailRentas =
                detailEntities.stream()
                        .map(DetailRentaDTO::fromEntity)
                        .toList();

        RentaTotalesResponseDTO totals
                = RentaTotalesResponseDTO.calculateTotals(eventEntity,detailEntities,abonos);

        return new RentaDetailDTO(
                EventDTO.fromEntity(eventEntity),detailRentas,totals,abonos);
    }

    @Override
    public List<EventDTO> getFromQuery(RentaFilterDTO rentaFilterDTO) {

        Specification<Event> spec =
                RentaSpecification.applyFilter(rentaFilterDTO);

        List<Event> events = eventRepository.findAll(spec);

        return events.stream()
                // Mapea cada entidad Renta a un RentaResponseDTO
                .map(EventDTO::fromEntity)
                // Colecta los resultados en una nueva lista
                .toList();

    }
}
