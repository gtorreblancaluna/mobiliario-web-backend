package com.mx.gaby.mobiliario_web;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

@SpringBootApplication
@EnableEncryptableProperties
public class MobiliarioWebApplication {

    //private static final String MASTER_KEY = "mypassword";

	public static void main(String[] args) {
		SpringApplication.run(MobiliarioWebApplication.class,args);

        /*StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(MASTER_KEY);

        String textoClaro = "03318976";

        // Genera el texto cifrado
        String textoCifrado = encryptor.encrypt(textoClaro);

        // 2. Descifrar
        String textoDescifrado = encryptor.decrypt(textoCifrado);


        System.out.println("-------------------------------------");
        System.out.println("Texto Original: " + textoClaro);
        System.out.println("Texto Cifrado: ENC(" + textoCifrado + ")");
        System.out.println("Descifrado:  " + textoDescifrado);
        System.out.println("-------------------------------------");

         */
	}

}
