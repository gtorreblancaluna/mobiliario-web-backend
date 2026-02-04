package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.records.ClientDTO;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getAll();
    ClientDTO save (ClientDTO clientDTO);
}
