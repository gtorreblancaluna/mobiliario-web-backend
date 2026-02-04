package com.mx.gaby.mobiliario_web.constants;

public final class MessageConstant {

    private MessageConstant() {}

    public static final String TASK_WAREHOUSE_NO_USERS_FOUND =
            "No se generaron tareas para los encargados de almacen porque no se obtuvieron usuarios de la base de datos.";

    public static final String USER_NOT_FOUND_LOGIN
            = "Credenciales inválidas o usuario no encontrado.";

    public static final String MESSAGE_GENERATE_TASK_ENCARGADO_ALMACEN
            = "Tarea 'entrega chofer' generada para el encargado de almacen: {0}";

    public static final String MESSAGE_GENERATE_TASK_CHOFER
            = "Tarea 'entrega chofer' generada para el chofer: {0}";

    public static final String TASK_ALMACEN_SUCCESSFULY_CREATED =
            "Tarea almacen creada con éxito para el usuario: {0}";
}
