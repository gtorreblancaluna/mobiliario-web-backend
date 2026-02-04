package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.DeliveryDriverTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoferDeliveryTaskRepository extends JpaRepository<DeliveryDriverTask, Integer>{
}
