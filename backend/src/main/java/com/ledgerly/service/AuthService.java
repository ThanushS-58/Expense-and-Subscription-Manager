package com.ledgerly.service;

import com.ledgerly.dto.AuthResponse;
import com.ledgerly.dto.LoginRequest;
import com.ledgerly.dto.RegisterRequest;
import com.ledgerly.dto.UserResponse;
import com.ledgerly.entity.User;
import com.ledgerly.exception.ResourceNotFoundException;
import com.ledgerly.repository.UserRepository;
import com.ledgerly.security.JwtService;
import com.ledgerly.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        User user = userRepository.save(new User(request.name().trim(), email,
                passwordEncoder.encode(request.password())));
        return createResponse(new UserPrincipal(user));
    }

    public AuthResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password()));
        return createResponse((UserPrincipal) authentication.getPrincipal());
    }

    @Transactional(readOnly = true)
    public UserResponse profile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(user);
    }

    private AuthResponse createResponse(UserPrincipal principal) {
        return new AuthResponse(jwtService.generateToken(principal), toResponse(principal.getUser()));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }
}