package com.mx.gaby.mobiliario_web.constants;

public final class LogConstant {

    private LogConstant() {}

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
}
