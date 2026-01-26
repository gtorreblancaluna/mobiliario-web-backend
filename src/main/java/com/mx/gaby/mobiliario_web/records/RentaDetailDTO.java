package com.mx.gaby.mobiliario_web.records;

import java.util.List;

public record RentaDetailDTO(
        EventDTO event,
        List<DetailRentaDTO> detail,
        RentaTotalesResponseDTO totals,
        List<PaymentDTO> payments) {
}