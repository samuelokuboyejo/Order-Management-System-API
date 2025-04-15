package com.project.simple_order.service.impl;

import com.project.simple_order.auth.entities.User;
import com.project.simple_order.auth.repository.UserRepository;
import com.project.simple_order.dto.UserDto;
import com.project.simple_order.mapper.EntityDtoMapper;
import com.project.simple_order.service.UserService;
import com.project.simple_order.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final EntityDtoMapper entityDtoMapper;
    private final UserRepository userRepository;

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email '" + email + "' not found"));
    }

    @Override
    public AppResponse getUserDetailsWithOrderHistory() {
        User authenticatedUser = getAuthenticatedUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusOrderHistory(authenticatedUser);

        return AppResponse.builder()
                .status(HttpStatus.OK.value())
                .user(userDto)
                .build();
    }
}
