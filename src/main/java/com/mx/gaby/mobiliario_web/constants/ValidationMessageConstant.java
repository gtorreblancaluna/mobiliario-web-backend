package com.mx.gaby.mobiliario_web.constants;

public final class ValidationMessageConstant {

    private ValidationMessageConstant() {}

    public static final String FORMAT_DATE_DD_MM_YYYY_INVALID
            = "Fecha invalida, por favor considera mandar la fecha con este formato dd/MM/YYYY " +
            "o bien revisa si la fecha es valida.";

    public static final String END_DATE_IS_BEFORE_THAT_INIT_DATE
            = "Fecha inicial debe ser menor que fecha final.";

    public static final String LIMIT_ONE_YEAR_DATES
            = "Limite de fecha excedido. La fechas fechas no debe exceder un año.";

}
