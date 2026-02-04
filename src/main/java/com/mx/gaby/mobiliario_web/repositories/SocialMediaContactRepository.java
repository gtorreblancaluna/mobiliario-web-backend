package com.mx.gaby.mobiliario_web.repositories;

import com.mx.gaby.mobiliario_web.model.entitites.SocialMediaContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialMediaContactRepository
        extends JpaRepository<SocialMediaContact, Integer>{

    List<SocialMediaContact> findByIsActiveTrueOrderByDescriptionAsc ();

}
