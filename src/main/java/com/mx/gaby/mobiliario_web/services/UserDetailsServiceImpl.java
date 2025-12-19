package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import com.mx.gaby.mobiliario_web.constants.MessageConstant;
import com.mx.gaby.mobiliario_web.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Carga el User (que implementa UserDetails) desde JPA
        return userRepository.findByUsernameAndFgActive(username, ApplicationConstant.ONE)
                .orElseThrow(() ->
                        new UsernameNotFoundException(MessageConstant.USER_NOT_FOUND_LOGIN));
    }
}
