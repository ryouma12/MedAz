package com.example.medaz.service;


import com.example.medaz.dto.auth.JwtResponse;
import com.example.medaz.dto.auth.LoginRequest;
import com.example.medaz.dto.auth.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> registerUser(RegisterRequest registerRequest);
    ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest);
    // Add methods for password reset if needed
}