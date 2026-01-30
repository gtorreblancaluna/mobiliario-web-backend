package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.CacheConstant;
import com.mx.gaby.mobiliario_web.constants.LogConstant;
import com.mx.gaby.mobiliario_web.exceptions.BusinessException;
import com.mx.gaby.mobiliario_web.model.entitites.User;
import com.mx.gaby.mobiliario_web.records.UserDTO;
import com.mx.gaby.mobiliario_web.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = CacheConstant.USERS_IN_CATEGORIES_BY_EVENT_CACHE_KEY)
    public List<UserDTO> getUsersInCategoriesAlmacenAndEvent(final Integer eventId)
            throws BusinessException {
        log.info(LogConstant.USERS_IN_CATEGORIES_BY_EVENT_GETTING_FROM_BD);
        return userRepository.getUsersInCategoriesAlmacenAndEvent(eventId)
                .stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

    @Override
    @Cacheable(value = CacheConstant.WAREHOUSE_MANAGERS_CACHE_KEY)
    public List<UserDTO> getEncargadosDeAlmacenUsers ()
            throws BusinessException {
        log.info(LogConstant.WAREHOUSE_MANAGERS_GETTING_FROM_BD);
        return userRepository.findByFgWarehouseManagerTrueAndFgActiveTrue()
                .stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

    @Override
    @Cacheable(value = CacheConstant.CHOFERES_CACHE_KEY)
    public List<UserDTO> getChoferes() throws BusinessException {
        log.info(LogConstant.CHOFERES_GETTING_FROM_BD);
        return userRepository.findByPositionIdAndFgActiveTrue(ApplicationConstant.PUESTO_CHOFER)
                .stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

    @Override
    public UserDTO getAuthenticatedUser() {
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {

            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                return UserDTO.fromEntity((User) principal);
            }
        }
        // Si no hay sesión, lanzas una excepción controlada o devuelves null según tu lógica
        throw new SecurityException("No existe un usuario autenticado en el contexto actual.");
    }

}
