package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.Abono;
import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbonoRepository extends JpaRepository<Abono, Integer>
{

    List<Abono> findByRentaId(Integer rentaId);

}
