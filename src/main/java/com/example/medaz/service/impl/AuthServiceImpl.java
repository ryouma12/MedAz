package com.example.medaz.service.impl;

import com.example.medaz.dto.auth.JwtResponse;
import com.example.medaz.dto.auth.LoginRequest;
import com.example.medaz.dto.auth.RegisterRequest;
import com.example.medaz.entity.Role;
import com.example.medaz.entity.RoleName;
import com.example.medaz.entity.User;
import com.example.medaz.exception.ResourceNotFoundException;
import com.example.medaz.repository.RoleRepository;
import com.example.medaz.repository.UserRepository;
import com.example.medaz.security.jwt.JwtUtils;
import com.example.medaz.security.services.UserDetailsImpl;
import com.example.medaz.service.AuthService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    @Transactional
    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByIdentificationNumber(registerRequest.getIdentificationNumber())) {
            logger.warn("Registration attempt with existing identification number: {}", registerRequest.getIdentificationNumber());
            return ResponseEntity.badRequest().body("Error: Identification number is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.warn("Registration attempt with existing email: {}", registerRequest.getEmail());
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setIdentificationNumber(registerRequest.getIdentificationNumber());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setHeight(registerRequest.getHeight());
        user.setWeight(registerRequest.getWeight());
        user.setGender(registerRequest.getGender());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> {
                    logger.error("FATAL ERROR: Role ROLE_USER not found in database!");
                    return new ResourceNotFoundException("Role", "name", RoleName.ROLE_USER.name());
                });
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        logger.info("User registered successfully with ROLE_USER: {}", user.getIdentificationNumber());
        return ResponseEntity.ok("User registered successfully!");
    }

    @Override
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        logger.debug("Attempting authentication for user: {}", loginRequest.getIdentificationNumber());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getIdentificationNumber(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        logger.info("User authenticated successfully: {} with roles: {}", userDetails.getUsername(), roles);
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                roles));
    }
}