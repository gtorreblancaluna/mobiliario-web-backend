package com.mx.gaby.mobiliario_web.configs;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ProfileLogger {

    private static final Logger log = LoggerFactory.getLogger(ProfileLogger.class);


    private final Environment environment;

    public ProfileLogger(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void logActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        log.info("##########################################################");
        if (activeProfiles.length == 0) {
            log.warn("!!! NO HAY PERFILES ACTIVOS - Usando perfil 'default' !!!");
        } else {
            if (log.isInfoEnabled()) {
                // El método toString() solo se invoca si el log realmente se va a escribir
                log.info("🚀 Perfiles activos detectados: {}", Arrays.toString(activeProfiles));
            }
        }
        log.info("##########################################################");
    }
}
