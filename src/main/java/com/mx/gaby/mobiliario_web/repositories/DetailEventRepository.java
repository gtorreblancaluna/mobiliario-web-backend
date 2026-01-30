package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.DetailRenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailEventRepository extends JpaRepository<DetailRenta, Integer>
{

    List<DetailRenta> findByEventId(Integer eventId);

}
