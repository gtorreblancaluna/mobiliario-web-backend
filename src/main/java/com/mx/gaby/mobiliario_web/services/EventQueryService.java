package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.records.RentaDetailDTO;
import com.mx.gaby.mobiliario_web.records.RentaFilterDTO;
import com.mx.gaby.mobiliario_web.records.EventDTO;
import java.util.List;

public interface EventQueryService {

    RentaDetailDTO findById (Integer id);
    List<EventDTO> getFromQuery (RentaFilterDTO rentaFilterDTO);

}
