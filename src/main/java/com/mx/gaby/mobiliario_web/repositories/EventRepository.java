package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.Event;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>,
        JpaSpecificationExecutor<Event>
{

    @NonNull
    Optional<Event> findById(Integer id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Event e SET
        e.state.id = :#{#event.state.id},
        e.customer.id = :#{#event.customer.id},
        e.fechaEntrega = :#{#event.fechaEntrega},
        e.fechaEvento = :#{#event.fechaEvento},
        e.horaEntrega = :#{#event.horaEntrega},
        e.fechaDevolucion = :#{#event.fechaDevolucion},
        e.horaDevolucion = :#{#event.horaDevolucion},
        e.descripcion = :#{#event.descripcion},
        e.porcentajeDescuento = :#{#event.porcentajeDescuento},
        e.iva = :#{#event.iva},
        e.comentario = :#{#event.comentario},
        e.chofer.id = :#{#event.chofer.id},
        e.type.id = :#{#event.type.id},
        e.depositoGarantia = :#{#event.depositoGarantia},
        e.envioRecoleccion = :#{#event.envioRecoleccion},
        e.mostrarPreciosPdf = :#{#event.mostrarPreciosPdf},
        e.updatedAt = :#{#event.updatedAt}
        WHERE e.id = :#{#event.id}
        """)
    void updateEvent(@Param("event") Event event);

}
