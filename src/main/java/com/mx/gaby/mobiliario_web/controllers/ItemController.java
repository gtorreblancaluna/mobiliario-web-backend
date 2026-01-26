package com.mx.gaby.mobiliario_web.controllers;

import com.mx.gaby.mobiliario_web.records.ItemDTO;
import com.mx.gaby.mobiliario_web.services.ItemService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@Log4j2
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemDTO>> getAll() {
        List<ItemDTO> items = itemService.getAll();
        return ResponseEntity.ok(items);
    }
}
