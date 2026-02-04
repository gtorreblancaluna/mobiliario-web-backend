package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.EventDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailEventRepository extends JpaRepository<EventDetail, Integer>
{

    List<EventDetail> findByEventId(Integer eventId);

}
