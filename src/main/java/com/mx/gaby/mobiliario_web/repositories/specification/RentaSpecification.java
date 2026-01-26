package com.mx.gaby.mobiliario_web.repositories.specification;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.model.entitites.Event;
import com.mx.gaby.mobiliario_web.records.RentaFilterDTO;
import com.mx.gaby.mobiliario_web.utils.ValidateUtil;
import jakarta.persistence.criteria.Expression;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log4j2
public class RentaSpecification {

    private RentaSpecification () {}

    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern(ApplicationConstant.FORMAT_DATE_DD_MM_YYY);

    // Metodo principal para construir la Specification combinada
    public static Specification<Event> applyFilter(RentaFilterDTO rentaFilterDTO) {

        // Inicializa la Specification con el primer criterio.
        // Si tienesNombre() devuelve null (porque nombre es nulo), el where() actúa como un 'true'

        Specification<Event> spec = Specification.where(all());

        boolean filterApplied = false;

        // Encadenar los demás criterios con AND
        if (rentaFilterDTO.initFechaEntrega() != null
                && rentaFilterDTO.endFechaEntrega() != null) {

            ValidateUtil.validateInitAndEndDate(
                    rentaFilterDTO.initFechaEntrega(),
                    rentaFilterDTO.endFechaEntrega());
            filterApplied = true;
            spec = spec.and(
                            betweenInDates
                                    (rentaFilterDTO.initFechaEntrega(),
                                            rentaFilterDTO.endFechaEntrega(),
                                            ApplicationConstant.FECHA_ENTREGA_ENTITY));
        }

        if (!filterApplied) {
            // si ningun filtro se aplico, entonces aplicamos el filtro por default.
            spec = spec.and(todayGreaterThanDate(ApplicationConstant.FECHA_ENTREGA_ENTITY));
        }

        spec = spec.and(orderByDesc(ApplicationConstant.FECHA_ENTREGA_ENTITY));

        return spec;
    }

    /**
     * Devuelve una Specification que no aplica filtros (WHERE 1=1).
     * Garantiza que la Specification devuelta NUNCA es null.
     */
    public static Specification<Event> all() {
        // builder.conjunction() crea un predicado 'TRUE' constante.
        // No requiere procesamiento de columnas, por lo que es óptimo.
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Event> orderByDesc(String nameColumnInEntity) {


        return (root, query, builder) -> {

            Expression<LocalDate> dateDBConverted = builder.function(
                    ApplicationConstant.STR_TO_DATE,
                    LocalDate.class,
                    root.get(nameColumnInEntity),
                    builder.literal(ApplicationConstant.DB_DATE_FORMAT)
            );

            query.orderBy(builder.desc(dateDBConverted));

            return null;

        };
    }

    /**
     * Filtra las Renta donde la fechaEntrega (VARCHAR) está dentro del rango.
     */
    public static Specification<Event> todayGreaterThanDate(String nameColumnInEntity) {

        log.debug(LogConstant.GREATER_THAN_TODAY_DATE_FILTER_APPLYING);

        final String TODAY_STRING = LocalDate.now().format(FORMATTER);

        return (root, query, builder) -> {

            Expression<LocalDate> dateDBConverted = builder.function(
                    ApplicationConstant.STR_TO_DATE,
                    LocalDate.class,
                    root.get(nameColumnInEntity),
                    builder.literal(ApplicationConstant.DB_DATE_FORMAT) // <-- Formato de la fecha en la columna
            );

            Expression<LocalDate> todayConverted = builder.function(
                    ApplicationConstant.STR_TO_DATE,
                    LocalDate.class,
                    builder.literal(TODAY_STRING),
                    builder.literal(ApplicationConstant.DB_DATE_FORMAT) // <-- Formato de la fecha en la columna
            );

            return builder.greaterThan(dateDBConverted, todayConverted);
        };
    }

    /**
     * Filtra las Renta donde la fechaEntrega (VARCHAR) está dentro del rango.
     */
    public static Specification<Event> betweenInDates(
            String initFechaEntrega, String endFechaEntrega, final String nameColumnInEntity) {

        if (initFechaEntrega == null || endFechaEntrega == null) {
            return null; // El filtro requiere ambas fechas para el rango
        }

        log.debug(LogConstant.BETWEEN_INIT_AND_END_DATE_FILTER_APPLYING);

        return (root, query, builder) -> {

            // 1. Convertir la columna de la base de datos (VARCHAR) a DATE.
            // Argumentos: Nombre de la función SQL, Tipo de retorno, Argumentos de la función.
            Expression<java.sql.Date> dateFromColumn = builder.function(
                    ApplicationConstant.STR_TO_DATE,
                    java.sql.Date.class,
                    root.get(nameColumnInEntity),
                    builder.literal(ApplicationConstant.DB_DATE_FORMAT) // <-- Formato de la fecha en la columna
            );

            // 2. Convertir las fechas de parámetro (String) a DATE para la comparación.
            Expression<java.sql.Date> dateFromInitParam = builder.function(
                    ApplicationConstant.STR_TO_DATE,
                    java.sql.Date.class,
                    builder.literal(initFechaEntrega),
                    builder.literal(ApplicationConstant.DB_DATE_FORMAT)
            );

            Expression<java.sql.Date> dateFromEndParam = builder.function(
                    ApplicationConstant.STR_TO_DATE,
                    java.sql.Date.class,
                    builder.literal(endFechaEntrega),
                    builder.literal(ApplicationConstant.DB_DATE_FORMAT)
            );

            // 3. Aplicar la comparación BETWEEN a las expresiones convertidas
            return builder.between(dateFromColumn, dateFromInitParam, dateFromEndParam);
        };
    }


}
