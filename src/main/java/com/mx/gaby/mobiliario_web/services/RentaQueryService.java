package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.records.RentaFilterDTO;
import com.mx.gaby.mobiliario_web.records.RentaResponseDTO;
import java.util.List;

public interface RentaQueryService {

    RentaResponseDTO findById (Integer id);
    List<RentaResponseDTO> getFromQuery (RentaFilterDTO rentaFilterDTO);

}
