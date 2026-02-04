package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.records.SocialMediaContactRecord;

import java.util.List;

public interface SocialMediaContactService {

    List<SocialMediaContactRecord> findAllActive();
}
