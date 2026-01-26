package com.mx.gaby.mobiliario_web;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableEncryptableProperties
@EnableCaching
public class MobiliarioWebApplication {


	public static void main(String[] args) {
		SpringApplication.run(MobiliarioWebApplication.class,args);
	}

}
