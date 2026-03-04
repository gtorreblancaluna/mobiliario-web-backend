package com.mx.gaby.mobiliario_web.controllers;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.records.EventDetailDTO;
import com.mx.gaby.mobiliario_web.records.EventFilterDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import com.mx.gaby.mobiliario_web.services.EventQueryService;
import com.mx.gaby.mobiliario_web.services.impl.EventSaveServiceImpl;
import com.mx.gaby.mobiliario_web.services.impl.EventUpdateServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@Log4j2
public class EventController {

    private final EventQueryService eventQueryService;

    private final EventUpdateServiceImpl eventUpdateService;
    private final EventSaveServiceImpl eventSaveService;

    public EventController(
            EventQueryService eventQueryService,
            EventUpdateServiceImpl eventUpdateService,
            EventSaveServiceImpl eventSaveService) {
        this.eventQueryService = eventQueryService;
        this.eventUpdateService = eventUpdateService;
        this.eventSaveService = eventSaveService;
    }

    @PostMapping("/save")
    public ResponseEntity<Integer> save(
            @Valid @RequestBody EventDetailDTO eventDetailDTO)
            throws BusinessException {

        Integer eventId;

        if (eventDetailDTO.event().id() != null && eventDetailDTO.event().id() > 0) {
            log.info(LogConstant.INIT_UPDATE_EVENT, eventDetailDTO);
            eventId = eventUpdateService.executeSaveTemplate(eventDetailDTO);
        } else {
            log.info(LogConstant.INIT_SAVE_EVENT, eventDetailDTO);
            eventId = eventSaveService.executeSaveTemplate(eventDetailDTO);
        }

        return ResponseEntity.ok(eventId);
    }

    @PostMapping
    public ResponseEntity<List<EventDTO>> getByFilter(
            @RequestBody EventFilterDTO eventFilterDTO) {

        log.info(LogConstant.INIT_GET_EVENT_BY_FILTER, eventFilterDTO);

        List<EventDTO> eventDTOList =
                eventQueryService.getFromQuery(eventFilterDTO);

        return ResponseEntity.ok(eventDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailDTO> getEventById(@PathVariable Integer id) {
        return ResponseEntity.ok(eventQueryService.findById(id));
    }

}
