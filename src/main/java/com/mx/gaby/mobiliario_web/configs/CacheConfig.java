package com.mx.gaby.mobiliario_web.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.mx.gaby.mobiliario_web.constants.CacheConstant;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // Configuramos los nombres de los cachés que definiste
        cacheManager.setCacheNames(
                List.of(
                        CacheConstant.ITEMS_CACHE_KEY,
                        CacheConstant.TYPE_PAYMENTS_CACHE_KEY,
                        CacheConstant.WAREHOUSE_MANAGERS_CACHE_KEY,
                        CacheConstant.CHOFERES_CACHE_KEY,
                        CacheConstant.USERS_IN_CATEGORIES_BY_EVENT_CACHE_KEY));

        // Aplicamos la política de Caffeine
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(560, TimeUnit.MINUTES) // El catálogo se refresca en minutos
                .maximumSize(4000) // Límite de objetos en RAM para no saturar tu instancia de GCP
                .recordStats()); // Permite ver métricas después

        return cacheManager;
    }
}