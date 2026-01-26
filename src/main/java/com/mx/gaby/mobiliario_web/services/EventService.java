package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;
import com.mx.gaby.mobiliario_web.model.entitites.Payment;
import com.mx.gaby.mobiliario_web.records.DetailRentaDTO;
import com.mx.gaby.mobiliario_web.records.PaymentDTO;
import com.mx.gaby.mobiliario_web.records.RentaDetailDTO;
import com.mx.gaby.mobiliario_web.repositories.DetailRentaRepository;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.utils.ValidateUtil;
import lombok.extern.log4j.Log4j2;

import java.text.MessageFormat;
import java.util.List;

@Log4j2
public abstract class EventService {

    protected final DetailRentaRepository detailRentaRepository;
    protected final PaymentRepository paymentRepository;

    protected EventService(DetailRentaRepository detailRentaRepository, PaymentRepository paymentRepository) {
        this.detailRentaRepository = detailRentaRepository;
        this.paymentRepository = paymentRepository;
    }

    protected void savePayments (final RentaDetailDTO rentaDetailDTO)
            throws BusinessException {

        if (rentaDetailDTO.payments().isEmpty()) {
            return;
        }

        try {

            List<Payment> payments = rentaDetailDTO
                    .payments()
                    .stream()
                    .map(dto -> PaymentDTO.fromDTO(dto, rentaDetailDTO.event().id()))
                    .toList();

            paymentRepository.saveAll(payments);

            log.info(LogConstant.PAYMENTS_UPDATED_SUCCESSFULLY, rentaDetailDTO.event().folio());

        } catch (Exception exception) {

            log.error(LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_PAYMENTS_IN_EVENT,rentaDetailDTO.event().folio());
            throw new BusinessException(
                    MessageFormat.format(
                            LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_PAYMENTS_IN_EVENT,rentaDetailDTO.event()
                                    .folio()),exception);

        }
    }

    protected void saveDetails (final RentaDetailDTO rentaDetailDTO)
            throws BusinessException {

        try {
            List<DetailRenta> detailRentaList
                    = rentaDetailDTO
                    .detail()
                    .stream()
                    .map(detailRentaDTO ->
                         (DetailRentaDTO.fromDTO(detailRentaDTO, rentaDetailDTO.event().id()))
                    )
                    .toList();

            detailRentaRepository.saveAll(detailRentaList);

            log.info(LogConstant.RENTA_DETAIL_UPDATED_SUCCESSFULLY, rentaDetailDTO.event().folio());

        } catch (Exception exception) {
            log.error(LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_RENTA_DETAIL, rentaDetailDTO.event().folio());
            throw new BusinessException(
                    MessageFormat.format(
                            LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_RENTA_DETAIL,rentaDetailDTO.event()
                                    .folio()),exception);
        }
    }

    private static void validateStatusAndTypeEvent(
            String statusId, String typeId)
            throws BusinessException {

        // Regla 1: Cotización debe ser Pendiente
        if (typeId.equals(ApplicationConstant.TIPO_COTIZACION)
                && !statusId.equals(ApplicationConstant.ESTADO_PENDIENTE)) {

            throw new BusinessException(MessageFormat.format(
                    ValidationMessageConstant.ERROR_COTIZACION_STATUS,
                    ApplicationConstant.DS_TIPO_COTIZACION,
                    ApplicationConstant.DS_ESTADO_PENDIENTE
            ));
        }

        // Regla 2: Pedido y Fabricación NO deben ser Pendientes
        boolean isPedidoOFabricacion = typeId.equals(ApplicationConstant.TIPO_PEDIDO)
                || typeId.equals(ApplicationConstant.TIPO_FABRICACION);

        if (isPedidoOFabricacion
                && statusId.equals(ApplicationConstant.ESTADO_PENDIENTE)) {

            // Determinamos la descripción del tipo para el mensaje
            String dsTipo = typeId.equals(ApplicationConstant.TIPO_PEDIDO)
                    ? ApplicationConstant.DS_TIPO_PEDIDO
                    : ApplicationConstant.DS_TIPO_FABRICACION;

            throw new BusinessException(MessageFormat.format(
                    ValidationMessageConstant.ERROR_TYPE_MUST_NOT_BE_PENDING,
                    dsTipo,
                    ApplicationConstant.DS_ESTADO_PENDIENTE
            ));
        }
    }

    private void validate (final RentaDetailDTO rentaDetailDTO)
            throws BusinessException {

        validateStatusAndTypeEvent(rentaDetailDTO.event().estadoId().toString(),
                rentaDetailDTO.event().tipoId().toString());

        ValidateUtil.isValidRange(rentaDetailDTO.event().horaEntrega());

        ValidateUtil.isValidRange(rentaDetailDTO.event().horaDevolucion());

        ValidateUtil.validateInitAndEndDate(
                rentaDetailDTO.event().fechaEntrega(), rentaDetailDTO.event().fechaDevolucion());

        float iva = 0f;
        if (rentaDetailDTO.event().iva() != null) {
            iva = rentaDetailDTO.event().iva();
        }
        if (iva < 0 || iva > 100) {
            throw new BusinessException(ValidationMessageConstant.IVA_NOT_VALID);
        }

        try {

            float discountPercentage = 0;

            if (rentaDetailDTO.event().porcentajeDescuento() != null) {
                discountPercentage
                        = Float.parseFloat(
                                rentaDetailDTO.event().porcentajeDescuento());
            }

            if (discountPercentage < 0 || discountPercentage > 100) {
                throw new BusinessException(ValidationMessageConstant.PERCENTAGE_DISCOUNT_NOT_VALID);
            }

        } catch (NumberFormatException numberFormatException) {
            throw new BusinessException(
                    ValidationMessageConstant.PARSE_NUMBER_EXCEPTION_PERCENTAGE_DISCOUNT);
        }

        if (rentaDetailDTO.event().descripcion() != null
                && !rentaDetailDTO.event().descripcion().isEmpty()
                && rentaDetailDTO.event().descripcion().length() > ValidationMessageConstant.LIMIT_LENGTH_STRING) {
            throw new BusinessException(ValidationMessageConstant.LIMIT_LENGTH_REACHED);
        }

        if (rentaDetailDTO.event().comentario() != null
                && !rentaDetailDTO.event().comentario().isEmpty()
                && rentaDetailDTO.event().comentario().length() > ValidationMessageConstant.LIMIT_LENGTH_STRING) {
            throw new BusinessException(ValidationMessageConstant.LIMIT_LENGTH_REACHED);
        }

        if (rentaDetailDTO.detail().isEmpty() ) {
            throw new BusinessException(
                    ValidationMessageConstant.ITEMS_NOT_BE_NULL);
        }

        String typeId = String.valueOf(rentaDetailDTO.event().tipoId());

        long countOfNewPayments = rentaDetailDTO.payments().stream()
                .filter(paymentDTO -> paymentDTO.id() == 0).count();

        if (typeId.equals(ApplicationConstant.TIPO_COTIZACION)
                && countOfNewPayments > 0) {

            String message = MessageFormat.format(
                    ValidationMessageConstant.PAYMENT_NOT_ALLOWED_BY_TYPE,
                    ApplicationConstant.DS_TIPO_COTIZACION,
                    ApplicationConstant.DS_TIPO_PEDIDO,
                    ApplicationConstant.DS_TIPO_FABRICACION
            );
            log.error(message);
            throw new BusinessException(message);
        }

    }

    abstract void save (final RentaDetailDTO rentaDetailDTO)
            throws BusinessException;

    // template method
    public void executeSaveTemplate (final RentaDetailDTO rentaDetailDTO)
            throws BusinessException {

        validate(rentaDetailDTO);
        // method 'save' will execute in child class.
        save(rentaDetailDTO);
    }
}
