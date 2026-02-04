package com.mx.gaby.mobiliario_web.services.impl;

import com.mx.gaby.mobiliario_web.constants.CacheConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.Client;
import com.mx.gaby.mobiliario_web.records.ClientDTO;
import com.mx.gaby.mobiliario_web.repositories.ClientRepository;
import com.mx.gaby.mobiliario_web.services.ClientService;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    @CacheEvict(value = CacheConstant.CLIENTS_CACHE_KEY, allEntries = true)
    @Override
    public ClientDTO save (final ClientDTO clientDTO) {

        Client entity = ClientDTO.fromDTO(clientDTO);

        if (clientDTO.rfc() != null && !clientDTO.rfc().isBlank()) {

            String finalRFC = clientDTO.rfc().trim().toUpperCase().replace("-", "");

            if (!finalRFC.matches("^[A-Z&Ñ]{3,4}\\d{6}[A-V1-9][A-Z1-9]\\d$")) {
                throw new BusinessException("El formato del RFC es incorrecto.");
            }

            entity.setTaxId(finalRFC);
        }

        entity.setActive(true);

        return ClientDTO.fromEntity(clientRepository.save(entity));
    }

    @Override
    @Cacheable(value = CacheConstant.CLIENTS_CACHE_KEY)
    public List<ClientDTO> getAll() {

        return clientRepository
                .findByIsActiveTrueOrderByFirstNameAsc()
                .stream()
                .map(ClientDTO::fromEntity)
                .toList();
    }

}
