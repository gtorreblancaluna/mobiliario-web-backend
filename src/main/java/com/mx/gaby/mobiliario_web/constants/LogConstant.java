package com.mx.gaby.mobiliario_web.constants;

public final class LogConstant {

    private LogConstant() {}


    public static final String ERROR_TRYING_UPDATE_OR_SAVE_PAYMENTS_IN_EVENT
            = "Ocurrio un error al intentar guardar los pagos del evento- Folio: {}.";

    public static final String ERROR_TRYING_UPDATE_OR_SAVE_RENTA_DETAIL
            = "Ocurrio un error al intentar guardar el detalle del evento. Folio: {}.";

    public static final String PAYMENTS_UPDATED_SUCCESSFULLY
            = "Pagos con renta folio {} actualizada con exito.";

    public static final String RENTA_DETAIL_UPDATED_SUCCESSFULLY
            = "Detalle renta con folio {} actualizada con exito.";

    public static final String RENTA_UPDATED_SUCCESSFULLY
            = "Renta folio {} actualizada con exito.";

    public static final String INIT_SAVE_RENTA
            = "Iniciando guardar renta, request body: {}";

    public static final String INIT_GET_RENTA_BY_FILTER
            = "Iniciando rentas por filtro, request body: {}";

    public static final String INIT_AUTH
            = "Iniciando autenticacion para el usuario: {}";

    public static final String USER_LOGIN_FAIL
            = "Fallo de autenticación para usuario: {}";

    public static final String GREATER_THAN_TODAY_DATE_FILTER_APPLYING
            = "Aplicando filtro fecha base de datos sea mayor a hoy.";

    public static final String BETWEEN_INIT_AND_END_DATE_FILTER_APPLYING
            = "Aplicando filtro entre fecha inicial y fecha final.";

    public static final String ITEMS_GETTING_FROM_BD
            = ">>> ACCEDIENDO A LA DB PARA OBTENER ARTÍCULOS...";

    public static final String TYPE_PAYMENTS_GETTING_FROM_BD
            = ">>> ACCEDIENDO A LA DB PARA OBTENER TIPOS DE PAGO...";
}
