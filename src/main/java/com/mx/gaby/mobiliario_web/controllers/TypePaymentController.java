package com.mx.gaby.mobiliario_web.controllers;


import com.mx.gaby.mobiliario_web.records.TypePaymentDTO;
import com.mx.gaby.mobiliario_web.services.TypePaymentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/type-payments")
@Log4j2
public class TypePaymentController {

    private final TypePaymentService typePaymentService;

    public TypePaymentController(TypePaymentService typePaymentService) {
        this.typePaymentService = typePaymentService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<TypePaymentDTO>> getAll() {
        List<TypePaymentDTO> items = typePaymentService.getAll();
        return ResponseEntity.ok(items);
    }
}
