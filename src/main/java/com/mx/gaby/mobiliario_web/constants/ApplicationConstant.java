package com.mx.gaby.mobiliario_web.constants;

public final class ApplicationConstant {

    private ApplicationConstant() {}

    /** Catalogo tipo de evento */
    public static final String TIPO_PEDIDO = "1";
    public static final String TIPO_COTIZACION = "2";
    public static final String TIPO_FABRICACION = "3";

    public static final String DS_TIPO_PEDIDO = "Renta";
    public static final String DS_TIPO_COTIZACION = "Cotización";
    public static final String DS_TIPO_FABRICACION = "Venta";

    /** Catalogo estados de renta */
    public static final String ESTADO_APARTADO = "1";
    public static final String ESTADO_EN_RENTA = "2";
    public static final String ESTADO_PENDIENTE = "3";
    public static final String ESTADO_CANCELADO = "4";
    public static final String ESTADO_FINALIZADO = "5";

    /** Descripcion estados de renta **/
    public static final String DS_ESTADO_APARTADO = "Apartado";
    public static final String DS_ESTADO_EN_RENTA = "En renta";
    public static final String DS_ESTADO_PENDIENTE = "Pendiente";
    public static final String DS_ESTADO_CANCELADO = "Cancelado";
    public static final String DS_ESTADO_FINALIZADO = "Finalizado";

    public static final String ONE = "1";
    public static final String BLANK_SPACE = " ";
    public static final String EMPTY_STRING = "";

    public static final String LANGUAGE = "es";
    public static final String COUNTRY = "MX";

    /** Formato almacenado en la base de datos */
    public static final String FORMAT_DATE_DD_MM_YYY = "dd/MM/yyyy";
    /** Formato corto para mostrar en la vista. */
    public static final String FORMAT_DATE_SHOW_IN_FRONT = "dd MMM yyyy";
    public static final String STR_TO_DATE = "STR_TO_DATE";
    public static final String DB_DATE_FORMAT = "%d/%m/%Y";
    public static final String FECHA_ENTREGA_ENTITY = "fechaEntrega";

    public static final String FG_ACTIVE_TRUE = "1";
}
