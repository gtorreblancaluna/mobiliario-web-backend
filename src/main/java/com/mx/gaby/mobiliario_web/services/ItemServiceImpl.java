package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.CacheConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.records.ItemDTO;
import com.mx.gaby.mobiliario_web.repositories.ItemRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    @Cacheable(value = CacheConstant.ITEMS_CACHE_KEY)
    public List<ItemDTO> getAll() {
        log.info(LogConstant.ITEMS_GETTING_FROM_BD);
        return itemRepository
                .findAllByFgActiveTrue()
                .stream()
                .map(ItemDTO::fromEntity)
                .toList();
    }

}
