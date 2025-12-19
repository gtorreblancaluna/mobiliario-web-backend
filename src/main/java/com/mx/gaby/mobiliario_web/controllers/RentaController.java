package com.mx.gaby.mobiliario_web.controllers;

import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.records.RentaFilterDTO;
import com.mx.gaby.mobiliario_web.records.RentaResponseDTO;
import com.mx.gaby.mobiliario_web.services.RentaQueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renta")
@Log4j2
public class RentaController {

    private final RentaQueryService rentaQueryService;

    public RentaController(RentaQueryService rentaQueryService) {
        this.rentaQueryService = rentaQueryService;
    }

    @PostMapping
    public ResponseEntity<?> getByFilter(
            @RequestBody RentaFilterDTO rentaFilterDTO) throws Exception {

        log.info(LogConstant.INIT_GET_RENTA_BY_FILTER, rentaFilterDTO);

        List<RentaResponseDTO> rentaResponseDTOList =
                rentaQueryService.getFromQuery(rentaFilterDTO);

        return ResponseEntity.ok(rentaResponseDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentaById(@PathVariable Integer id) {
        return ResponseEntity.ok(rentaQueryService.findById(id));
    }

}
