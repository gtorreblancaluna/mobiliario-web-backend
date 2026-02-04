package com.mx.gaby.mobiliario_web.controllers;

import com.mx.gaby.mobiliario_web.records.SocialMediaContactRecord;
import com.mx.gaby.mobiliario_web.services.SocialMediaContactService;
import com.mx.gaby.mobiliario_web.services.impl.SocialMediaContactServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/social-media-contacts")
@Log4j2
public class SocialMediaContactController {

    private final SocialMediaContactService socialMediaContactService;

    public SocialMediaContactController(SocialMediaContactServiceImpl socialMediaContactService) {
        this.socialMediaContactService = socialMediaContactService;
    }


    @GetMapping
    public ResponseEntity<List<SocialMediaContactRecord>> getAll() {
        List<SocialMediaContactRecord> socialMediaContactRecords
                = socialMediaContactService.findAllActive();
        return ResponseEntity.ok(socialMediaContactRecords);
    }
}
