package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.records.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsersInCategoriesWarehouseAndEvent(Integer eventId)
            throws BusinessException;

    List<UserDTO> getWarehouseManagers()
            throws BusinessException;

    List<UserDTO> getDeliveryDrivers()
            throws BusinessException;

    UserDTO getAuthenticatedUser();
}
