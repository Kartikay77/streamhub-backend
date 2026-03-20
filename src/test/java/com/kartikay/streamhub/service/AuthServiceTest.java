package com.kartikay.streamhub.service;

import com.kartikay.streamhub.dto.RegisterRequest;
import com.kartikay.streamhub.entity.User;
import com.kartikay.streamhub.repository.UserRepository;
import com.kartikay.streamhub.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldSaveUser() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Kartikay");
        request.setEmail("kartikay@test.com");
        request.setPassword("secret");

        when(userRepository.existsByEmail("kartikay@test.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encoded");

        var response = authService.register(request);

        assertEquals("User registered successfully", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrowIfEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Kartikay");
        request.setEmail("kartikay@test.com");
        request.setPassword("secret");

        when(userRepository.existsByEmail("kartikay@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }
}
