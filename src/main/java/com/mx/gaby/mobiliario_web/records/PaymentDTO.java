package com.mx.gaby.mobiliario_web.records;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.model.entitites.Payment;
import com.mx.gaby.mobiliario_web.model.entitites.TypePayment;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public record PaymentDTO(
        Integer id,
        Integer rentaId,
        Integer userId,
        String userName,
        String date,
        String paymentDate,
        Float amount,
        String comment,
        Integer typeId,
        String typeDescription,
        Timestamp updatedAt
) {

    public static Payment fromDTO (PaymentDTO paymentDTO, Integer rentaId) {

        Payment payment = new Payment();

        Integer paymentId = null;
        String dateString;

        payment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // is update
        if (paymentDTO.id() != null && paymentDTO.id() > 0) {
            paymentId = paymentDTO.id();
            dateString = paymentDTO.date();
        } else {
            payment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            dateString = new SimpleDateFormat(
                    ApplicationConstant.FORMAT_DATE_DD_MM_YYY).format(new Date());
        }

        payment.setId(paymentId);
        payment.setRentaId(rentaId);
        payment.setPaymentDate(paymentDTO.paymentDate());
        payment.setAmount(paymentDTO.amount());
        payment.setComment(paymentDTO.comment());

        TypePayment typePayment = new TypePayment();
        typePayment.setId(paymentDTO.typeId());
        payment.setType(typePayment);

        payment.setDate(dateString);

        return payment;

    }
    /**
     * Mapea la entidad al Record DTO.
     */
    public static PaymentDTO fromEntity(Payment entity) {
        return new PaymentDTO(
                entity.getId(),
                entity.getRentaId(),
                entity.getUser().getId(),
                entity.getUser().getName() + ApplicationConstant.BLANK_SPACE + entity.getUser().getLastName(),
                entity.getDate(),
                entity.getPaymentDate(),
                entity.getAmount(),
                entity.getComment(),
                (entity.getType() != null) ? entity.getType().getId() : null,
                (entity.getType() != null) ? entity.getType().getDescription() : null,
                entity.getUpdatedAt()
        );
    }
}
