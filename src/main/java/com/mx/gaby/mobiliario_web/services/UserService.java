package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.records.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsersInCategoriesAlmacenAndEvent (Integer eventId)
            throws BusinessException;

    List<UserDTO> getEncargadosDeAlmacenUsers ()
            throws BusinessException;

    List<UserDTO> getChoferes ()
            throws BusinessException;

    UserDTO getAuthenticatedUser();
}
