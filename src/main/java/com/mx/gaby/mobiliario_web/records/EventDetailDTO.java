package com.mx.gaby.mobiliario_web.records;

import jakarta.validation.Valid;

import java.util.List;

public record EventDetailDTO(

        @Valid
        EventDTO event,

        @Valid
        List<DetailRentaDTO> detail,
        EventTotalsResponseDTO totals,

        @Valid
        List<PaymentDTO> payments) {
}