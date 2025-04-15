package com.project.simple_order.auth.service;

import com.project.simple_order.auth.entities.RefreshToken;
import com.project.simple_order.auth.entities.User;
import com.project.simple_order.auth.repository.RefreshTokenRepository;
import com.project.simple_order.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByPhoneNumber(username)
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        RefreshToken refreshToken = user.getRefreshToken();

        if(refreshToken ==null) {
            long refreshTokenValidity = 5*60*60*10000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);
        }
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken  refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new RuntimeException("Refresh token not found"));

        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token expired");
        }
        return refToken;
    }

    public void deleteByUser(User user) {
        try {
            refreshTokenRepository.findByUser(user)
                    .ifPresent(refreshTokenRepository::delete);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting refresh token for user: " + user.getEmail(), e);
        }
    }
}
