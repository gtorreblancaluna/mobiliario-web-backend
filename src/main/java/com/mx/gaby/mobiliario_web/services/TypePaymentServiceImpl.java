package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.CacheConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.model.entitites.TypePayment;
import com.mx.gaby.mobiliario_web.records.TypePaymentDTO;
import com.mx.gaby.mobiliario_web.repositories.TypePaymentRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class TypePaymentServiceImpl implements TypePaymentService{

    private final TypePaymentRepository typePaymentRepository;

    public TypePaymentServiceImpl(TypePaymentRepository typePaymentRepository) {
        this.typePaymentRepository = typePaymentRepository;
    }

    @Override
    @Cacheable(value = CacheConstant.TYPE_PAYMENTS_CACHE_KEY)
    public List<TypePaymentDTO> getAll() {
        log.info(LogConstant.TYPE_PAYMENTS_GETTING_FROM_BD);
        return typePaymentRepository
                .findAllByFgActiveTrueOrderByDescriptionAsc()
                .stream()
                .map(TypePaymentDTO::fromEntity)
                .toList();
    }
}
