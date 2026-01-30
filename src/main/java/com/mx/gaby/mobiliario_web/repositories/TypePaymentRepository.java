package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.TypePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypePaymentRepository extends JpaRepository<TypePayment, Integer> {
    List<TypePayment> findAllByFgActiveTrueOrderByDescriptionAsc();
}
