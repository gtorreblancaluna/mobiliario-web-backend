package com.mx.gaby.mobiliario_web.constants;

public final class ApplicationConstant {

    private ApplicationConstant() {}

    public static final int PUESTO_CHOFER = 1;
    public static final int PUESTO_REPARTIDOR = 2;
    public static final int PUESTO_ADMINISTRADOR = 3;
    public static final int PUESTO_MOSTRADOR = 4;

    /** Catalogo para status tareas de almacen.*/

    public static final String TASK_NEW_FOLIO = "NUEVO FOLIO";
    public static final String TASK_UPDATE_STATUS_FOLIO = "CAMBIO ESTADO FOLIO";
    public static final String TASK_UPDATE_TYPE_FOLIO = "CAMBIO TIPO FOLIO";
    public static final String TASK_UPDATE_TYPE_AND_STATUS_FOLIO = "CAMBIO TIPO Y ESTADO FOLIO";
    public static final String TASK_UPDATE_ITEMS_FOLIO = "CAMBIO ARTICULOS FOLIO";
    public static final String TASK_GENERAL_DATA_UPDATED = "CAMBIO DATOS GENERALES";

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

    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String BLANK_SPACE = " ";
    public static final String BREAK_LINE_HTML = "\n";
    public static final String LINE_HTML = "####################################";

    public static final String EMPTY_STRING = "";

    public static final String LANGUAGE = "es";
    public static final String COUNTRY = "MX";


    /** Formato almacenado en la base de datos */
    public static final String FORMAT_DATE_DD_MM_YYY = "dd/MM/yyyy";
    /** Formato corto para mostrar en la vista. */
    public static final String FORMAT_DATE_SHOW_IN_FRONT = "dd MMM yyyy";
    public static final String LARGE_FORMAT_DATE_SHOW_IN_FRONT = "dd/MM/yyyy HH:mm:ss";
    public static final String STR_TO_DATE = "STR_TO_DATE";
    public static final String DB_DATE_FORMAT = "%d/%m/%Y";
    public static final String FECHA_ENTREGA_ENTITY = "fechaEntrega";

    public static final String FG_ACTIVE_TRUE = "1";
}
