package com.mx.gaby.mobiliario_web.constants;

public final class LogConstant {

    private LogConstant() {}

    public static final String INIT_WORK_FLOW_TASK = "Iniciando flujo de tarea para evento: {}";
    public static final String END_WORK_FLOW_TASK = "Flujo de tarea finalizado exitosamente, evento: {}";

    public static final String ERROR_WHEN_GENERATE_TASK_EXECUTED
            = "Ocurrio un error al intentar generar las tareas para el folio: {}";

    public static final String MESSAGE_GENERATE_TASK_CHOFER =
            "Tarea 'entrega chofer' generada para el chofer: {}, Folio: {}";

    public static final String MESSAGE_GENERATE_TASK_ENCARGADO_ALMACEN =
            "Tarea 'entrega chofer' generada para el encargado de almacen: {}, Folio: {}";

    public static final String TASK_ALMACEN_SUCCESSFULY_CREATED =
            "Tarea almacen creada con éxito para el usuario: {}, folio: {}";

    public static final String ERROR_TRYING_UPDATE_OR_SAVE_PAYMENTS_IN_EVENT
            = "Ocurrio un error al intentar guardar los pagos del evento. Folio: {}.";

    public static final String ERROR_TRYING_UPDATE_OR_SAVE_EVENT_DETAIL
            = "Ocurrio un error al intentar guardar el detalle del evento. Folio: {}.";

    public static final String PAYMENTS_UPDATED_SUCCESSFULLY
            = "Pagos del evento con folio {} actualizada con exito.";

    public static final String EVENT_DETAIL_UPDATED_SUCCESSFULLY
            = "Detalle evento con folio {} actualizada con exito.";

    public static final String EVENT_UPDATED_SUCCESSFULLY
            = "Evento con folio {} actualizada con exito.";

    public static final String INIT_SAVE_EVENT
            = "Iniciando guardar evento, request body: {}";

    public static final String INIT_GET_EVENT_BY_FILTER
            = "Iniciando eventos por filtro, request body: {}";

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

    public static final String WAREHOUSE_MANAGERS_GETTING_FROM_BD
            = ">>> ACCEDIENDO A LA DB PARA OBTENER LOS ENCARGADOS DE ALMACEN...";

    public static final String USERS_IN_CATEGORIES_BY_EVENT_GETTING_FROM_BD
            = ">>> ACCEDIENDO A LA DB PARA OBTENER LOS USUARIOS POR CATEGORIA DE ALMACEN...";

    public static final String CHOFERES_GETTING_FROM_BD
            = ">>> ACCEDIENDO A LA DB PARA OBTENER LOS CHOFERES...";
}
