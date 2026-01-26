package com.mx.gaby.mobiliario_web.constants;

public final class ValidationMessageConstant {

    private ValidationMessageConstant() {}

    // En ValidationMessageConstant.java
    public static final String ERROR_COTIZACION_STATUS =
            "Evento tipo {0} debe tener estado {1}";

    public static final String ERROR_TYPE_MUST_NOT_BE_PENDING =
            "Evento tipo {0} debe tener estado diferente a {1}";

    public static final String PAYMENT_NOT_ALLOWED_BY_TYPE =
            "No puedes agregar pagos porque el evento es de tipo [{0}].\n" +
                    "Para poder agregar pagos, el evento debe ser de tipo [{1}] o [{2}].";

    public static final String IVA_NOT_VALID
            = "Formato de número para el iva no es el esperado. " +
            "Valores esperados son: 0 - 100 y solo admite enteros positivos. ";

    public static final String PERCENTAGE_DISCOUNT_NOT_VALID
            = "Formato de número para el porcebtaje de descuento no es el esperado. " +
            "Valores esperados son: 0 - 100 y solo admite enteros positivos. ";

    public static final String PARSE_NUMBER_EXCEPTION_PERCENTAGE_DISCOUNT
            = "Ocurrio un error al intentar validar porcentaje de descuento. " +
            "El formato de número no es el esperado. ( 0 - 100 ) solo admite enteros positivos. ";

    public static final String ITEMS_NOT_BE_NULL = "Ingresa al menos un articulo al evento.";

    public static final int LIMIT_LENGTH_STRING = 400;

    public static final String LIMIT_LENGTH_REACHED
            = "Has rebasado los caracteres permitidos [400 caracteres] para el campo " +
            "direccion o comentario.";

    public static final String REGEX_A = " a ";

    public static final String HOURS_REQUIRED = "Hora inicial y final son requeridas.";

    public static final String ERROR_PARSING_INIT_END_HOURS
            = "Ocurrio un error al validar hora inicial y final. Detalle del error: {0}.";

    public static final String INIT_HOUR_BE_BEFORE
            = "Hora inicial deber ser menor a hora final.";

    public static final String FORMAT_DATE_DD_MM_YYYY_INVALID
            = "Fecha invalida, por favor considera mandar la fecha con este formato dd/MM/YYYY " +
            "o bien revisa si la fecha es valida.";

    public static final String END_DATE_IS_BEFORE_THAT_INIT_DATE
            = "Fecha inicial debe ser menor que fecha final.";

    public static final String LIMIT_ONE_YEAR_DATES
            = "Limite de fecha excedido. La fechas fechas no debe exceder un año.";

}
