package com.mx.gaby.mobiliario_web.constants;

public final class MessageConstant {

    private MessageConstant() {}

    public static final String TASK_FOR_WAREHOUSE_MANAGER = "Logs generados" +
            " para las tareas creadas a los encargados de almacen.";

    public static final String TASK_FOR_CHOFER = "Logs generados" +
            " para las tareas creadas a los choferes.";

    public static final String TASK_FOR_USERS = "Logs generados" +
            " para las tareas creadas a los usuarios por categoria.";


    public static final String USER_NOT_FOUND_LOGIN
            = "Credenciales inválidas o usuario no encontrado.";

    public static final String MESSAGE_GENERATE_TASK_ENCARGADO_ALMACEN
            = "Tarea 'entrega chofer' generada para el encargado de almacen: {0}";

    public static final String MESSAGE_GENERATE_TASK_CHOFER
            = "Tarea 'entrega chofer' generada para el chofer: {0}";

    public static final String TASK_ALMACEN_SUCCESSFULY_CREATED =
            "Tarea almacen creada con éxito para el usuario: {0}";
}
