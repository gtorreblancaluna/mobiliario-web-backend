package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.Renta;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RentaRepository extends JpaRepository<Renta, Integer>,
        JpaSpecificationExecutor<Renta>
{

    @NonNull
    Optional<Renta> findById(Integer id);


}
