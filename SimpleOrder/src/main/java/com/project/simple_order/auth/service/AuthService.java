package com.project.simple_order.auth.service;

import com.project.simple_order.auth.entities.User;
import com.project.simple_order.auth.repository.UserRepository;
import com.project.simple_order.auth.utils.AuthResponse;
import com.project.simple_order.auth.utils.LoginRequest;
import com.project.simple_order.auth.utils.RegisterRequest;
import com.project.simple_order.auth.utils.UserUtils;
import com.project.simple_order.dto.UserDto;
import com.project.simple_order.mapper.EntityDtoMapper;
import com.project.simple_order.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthResponse registerUser(RegisterRequest registerRequest){
        Optional<User> prevUser = userRepository.findByEmail(registerRequest.getEmail());
        if (prevUser.isPresent() || userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())){
            return AuthResponse.builder()
                    .responseMessage(UserUtils.ACCOUNT_EXISTS_MESSAGE)
                    .build();
        }


        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .address(registerRequest.getAddress())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getPhoneNumber());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .responseMessage(UserUtils.ACCOUNT_CREATION_MESSAGE)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getIdentifier(),
                        loginRequest.getPassword())
        );

        var user = userRepository.findByEmail(loginRequest.getIdentifier())
                .or(() -> userRepository.findByPhoneNumber(loginRequest.getIdentifier()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getIdentifier());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .responseMessage(UserUtils.ACCOUNT_AUTHENTICATION_MESSAGE)
                .build();
    }



}
