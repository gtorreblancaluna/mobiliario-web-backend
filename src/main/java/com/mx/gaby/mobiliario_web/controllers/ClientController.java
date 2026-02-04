package com.mx.gaby.mobiliario_web.controllers;

import com.mx.gaby.mobiliario_web.records.ClientDTO;
import com.mx.gaby.mobiliario_web.services.ClientService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Log4j2
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientDTO> clients = clientService.getAll();
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> save(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO clientSaved = clientService.save(clientDTO);
        return ResponseEntity.ok(clientSaved);
    }
}
