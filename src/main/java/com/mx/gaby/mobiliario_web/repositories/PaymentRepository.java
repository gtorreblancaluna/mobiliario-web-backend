package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer>
{

    List<Payment> findByRentaId(Integer rentaId);

}
