package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.constants.ValidationMessageConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.EventDetail;
import com.mx.gaby.mobiliario_web.model.entitites.Payment;
import com.mx.gaby.mobiliario_web.records.*;
import com.mx.gaby.mobiliario_web.repositories.DetailEventRepository;
import com.mx.gaby.mobiliario_web.repositories.EventRepository;
import com.mx.gaby.mobiliario_web.repositories.PaymentRepository;
import com.mx.gaby.mobiliario_web.repositories.UserRepository;
import com.mx.gaby.mobiliario_web.utils.ValidateUtil;
import lombok.extern.log4j.Log4j2;

import java.text.MessageFormat;
import java.util.List;

@Log4j2
public abstract class EventService {

    protected final EventRepository eventRepository;
    protected final DetailEventRepository detailEventRepository;
    protected final PaymentRepository paymentRepository;
    protected final UserService userService;
    private final UserRepository userRepository;

    protected EventService(
            EventRepository eventRepository, DetailEventRepository detailEventRepository,
            PaymentRepository paymentRepository,
            UserService userService,
            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.detailEventRepository = detailEventRepository;
        this.paymentRepository = paymentRepository;
        this.userService = userService;
        this.userRepository = userRepository;

    }

    private void savePayments (final EventDetailDTO eventDetailDTO)
            throws BusinessException {

        if (eventDetailDTO.payments().isEmpty()) {
            return;
        }

        try {

            UserDTO userSession = userService.getAuthenticatedUser();

            List<Payment> payments = eventDetailDTO
                    .payments()
                    .stream()
                    .map(dto -> PaymentDTO.fromDTO(dto, eventDetailDTO.event().id()))
                    .toList();

            // identificar nuevos pagos y asignarle al usuario que esta en sesion.
            payments.forEach(payment -> {
                if (payment.getId() == null || payment.getId().equals(0)) {
                    // para evitar el error de "Unsaved transient entity"
                    payment.setUser(userRepository.getReferenceById(userSession.id()));
                }
            });

            paymentRepository.saveAll(payments);

            log.info(LogConstant.PAYMENTS_UPDATED_SUCCESSFULLY, eventDetailDTO.event().folio());

        } catch (Exception exception) {

            log.error(LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_PAYMENTS_IN_EVENT, eventDetailDTO.event().folio());
            throw new BusinessException(
                    MessageFormat.format(
                            LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_PAYMENTS_IN_EVENT, eventDetailDTO.event()
                                    .folio()),exception);

        }
    }

    private void saveDetails (final EventDetailDTO eventDetailDTO)
            throws BusinessException {

        try {
            List<EventDetail> eventDetailList
                    = eventDetailDTO
                    .detail()
                    .stream()
                    .map(detailRentaDTO ->
                         (DetailRentaDTO.fromDTO(detailRentaDTO, eventDetailDTO.event().id()))
                    )
                    .toList();

            detailEventRepository.saveAll(eventDetailList);

            log.info(LogConstant.EVENT_DETAIL_UPDATED_SUCCESSFULLY, eventDetailDTO.event().folio());

        } catch (Exception exception) {
            log.error(LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_EVENT_DETAIL, eventDetailDTO.event().folio());
            throw new BusinessException(
                    MessageFormat.format(
                            LogConstant.ERROR_TRYING_UPDATE_OR_SAVE_EVENT_DETAIL, eventDetailDTO.event()
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

    private void validate (final EventDetailDTO eventDetailDTO) {

        validateStatusAndTypeEvent(eventDetailDTO.event().estadoId().toString(),
                eventDetailDTO.event().tipoId().toString());

        ValidateUtil.isValidRange(eventDetailDTO.event().horaEntrega());

        ValidateUtil.isValidRange(eventDetailDTO.event().horaDevolucion());

        ValidateUtil.validateInitAndEndDate(
                eventDetailDTO.event().fechaEntrega(), eventDetailDTO.event().fechaDevolucion());

        float iva = 0f;
        if (eventDetailDTO.event().iva() != null) {
            iva = eventDetailDTO.event().iva();
        }
        if (iva < 0 || iva > 100) {
            throw new BusinessException(ValidationMessageConstant.IVA_NOT_VALID);
        }

        try {

            float discountPercentage = 0;

            if (eventDetailDTO.event().porcentajeDescuento() != null) {
                discountPercentage
                        = Float.parseFloat(
                                eventDetailDTO.event().porcentajeDescuento());
            }

            if (discountPercentage < 0 || discountPercentage > 100) {
                throw new BusinessException(ValidationMessageConstant.PERCENTAGE_DISCOUNT_NOT_VALID);
            }

        } catch (NumberFormatException numberFormatException) {
            throw new BusinessException(
                    ValidationMessageConstant.PARSE_NUMBER_EXCEPTION_PERCENTAGE_DISCOUNT);
        }

        if (eventDetailDTO.event().descripcion() != null
                && !eventDetailDTO.event().descripcion().isEmpty()
                && eventDetailDTO.event().descripcion().length() > ValidationMessageConstant.LIMIT_LENGTH_STRING) {
            throw new BusinessException(ValidationMessageConstant.LIMIT_LENGTH_REACHED);
        }

        if (eventDetailDTO.event().comentario() != null
                && !eventDetailDTO.event().comentario().isEmpty()
                && eventDetailDTO.event().comentario().length() > ValidationMessageConstant.LIMIT_LENGTH_STRING) {
            throw new BusinessException(ValidationMessageConstant.LIMIT_LENGTH_REACHED);
        }

        if (eventDetailDTO.detail().isEmpty() ) {
            throw new BusinessException(
                    ValidationMessageConstant.ITEMS_NOT_BE_NULL);
        }

        String typeId = String.valueOf(eventDetailDTO.event().tipoId());

        long countOfNewPayments = eventDetailDTO.payments().stream()
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

    protected abstract void save (final EventDetailDTO eventDetailDTO);

    protected abstract void generateTasks(
            final EventDetailDTO eventDetailDTO, EventDTO currentEventDTO) throws BusinessException;

    // template method
    public void executeSaveTemplate (
            final EventDetailDTO eventDetailDTO)
                throws BusinessException {

        validate(eventDetailDTO);
        // method 'save' will execute in child class.
        save(eventDetailDTO);

        saveDetails(eventDetailDTO);

        savePayments(eventDetailDTO);

    }

}
