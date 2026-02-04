package com.mx.gaby.mobiliario_web.records;

import java.util.List;

public record EventDetailDTO(
        EventDTO event,
        List<DetailRentaDTO> detail,
        EventTotalsResponseDTO totals,
        List<PaymentDTO> payments) {
}