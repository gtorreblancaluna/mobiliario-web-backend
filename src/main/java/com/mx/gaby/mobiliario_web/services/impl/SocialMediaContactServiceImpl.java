package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.CacheConstant;
import com.mx.gaby.mobiliario_web.records.SocialMediaContactRecord;
import com.mx.gaby.mobiliario_web.repositories.SocialMediaContactRepository;
import com.mx.gaby.mobiliario_web.services.SocialMediaContactService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialMediaContactServiceImpl
        implements SocialMediaContactService {

    private final SocialMediaContactRepository repository;

    public SocialMediaContactServiceImpl
            (SocialMediaContactRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = CacheConstant.SOCIAL_MEDIA_LIST_CACHE_KEY)
    public List<SocialMediaContactRecord> findAllActive() {
        return repository
                .findByIsActiveTrueOrderByDescriptionAsc()
                .stream()
                .map(SocialMediaContactRecord::fromEntity)
                .toList();

    }
}
