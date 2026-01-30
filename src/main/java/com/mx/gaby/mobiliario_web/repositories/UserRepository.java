package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsernameAndFgActiveTrue(String userName);

    @Query(value = """
    -- Primera fuente: Usuarios por Desglose de Almacén
    SELECT u.* FROM usuarios u
    WHERE u.id_usuarios IN (
        SELECT ac.id_usuarios FROM asigna_categoria ac 
        WHERE ac.id_categoria IN (
            SELECT art.id_categoria FROM desglose_almacen da
            INNER JOIN articulo art ON (da.item_relation_id = art.id_articulo)
            WHERE da.item_init_id IN (
                SELECT dr.id_articulo FROM detalle_renta dr WHERE dr.id_renta = :eventId
            )
        )
    )
    AND u.activo = 1

    UNION

    -- Segunda fuente: Usuarios por Categorías Directas del Evento
    SELECT u.* FROM usuarios u
    WHERE u.id_usuarios IN (
        SELECT ac.id_usuarios FROM asigna_categoria ac 
        WHERE ac.id_categoria IN (
            SELECT art.id_categoria FROM detalle_renta dr
            INNER JOIN articulo art ON (dr.id_articulo = art.id_articulo)
            WHERE dr.id_renta = :eventId
        )
    )
    AND u.activo = 1    
    ORDER BY nombre ASC
    """, nativeQuery = true)
    List<User> getUsersInCategoriesAlmacenAndEvent(@Param("eventId") Integer eventId);

    List<User> findByFgWarehouseManagerTrueAndFgActiveTrue();

    List<User> findByPositionIdAndFgActiveTrue(Integer idPuesto);
}
