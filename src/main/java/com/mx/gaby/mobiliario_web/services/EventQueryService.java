package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.records.EventDetailDTO;
import com.mx.gaby.mobiliario_web.records.EventFilterDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import java.util.List;

public interface EventQueryService {

    EventDetailDTO findById (Integer id);
    List<EventDTO> getFromQuery (EventFilterDTO eventFilterDTO);

}
