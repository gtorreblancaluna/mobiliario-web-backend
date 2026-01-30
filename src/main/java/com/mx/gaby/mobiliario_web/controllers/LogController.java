package com.mx.gaby.mobiliario_web.controllers;

import com.mx.gaby.mobiliario_web.services.MessageStorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final MessageStorageService storageService;

    public LogController(MessageStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public List<String> getLogs() {
        return storageService.getAllMessages();
    }
}
