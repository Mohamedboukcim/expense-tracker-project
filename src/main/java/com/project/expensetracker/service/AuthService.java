package com.project.expensetracker.service;

import com.project.expensetracker.dto.auth.AuthResponse;
import com.project.expensetracker.dto.auth.LoginRequest;
import com.project.expensetracker.dto.auth.RegisterRequest;
import com.project.expensetracker.entity.User;
import com.project.expensetracker.repository.UserRepository;
import com.project.expensetracker.security.JwtService;
import com.project.expensetracker.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadCredentialsException("Un compte existe déjà avec cet email");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(new UserPrincipal(saved));

        return new AuthResponse(token, saved.getEmail(), saved.getFullName());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Identifiants invalides"));

        String token = jwtService.generateToken(new UserPrincipal(user));

        return new AuthResponse(token, user.getEmail(), user.getFullName());
    }
}