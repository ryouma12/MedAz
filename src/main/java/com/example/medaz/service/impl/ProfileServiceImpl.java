package com.example.medaz.service.impl;

import com.example.medaz.dto.profile.ProfileDto;
import com.example.medaz.dto.profile.ProfileUpdateRequest;
import com.example.medaz.entity.User;
import com.example.medaz.exception.ResourceNotFoundException;
import com.example.medaz.repository.UserRepository;
import com.example.medaz.security.services.UserDetailsImpl;
import com.example.medaz.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ProfileDto getCurrentUserProfile() {
        User user = getCurrentAuthenticatedUser();
        return mapUserToProfileDto(user);
    }

    @Override
    @Transactional
    public ProfileDto updateCurrentUserProfile(ProfileUpdateRequest updateRequest) {
        User user = getCurrentAuthenticatedUser();
        logger.info("Updating profile for user ID: {}", user.getId());

        if (updateRequest.getEmail() != null) {
            if (!user.getEmail().equals(updateRequest.getEmail()) && userRepository.existsByEmail(updateRequest.getEmail())) {
                logger.warn("Profile update failed for user ID {}: Email {} already exists.", user.getId(), updateRequest.getEmail());
                throw new IllegalArgumentException("Error: Email is already in use!");
            }
            user.setEmail(updateRequest.getEmail());
            logger.debug("User ID {} updating email to: {}", user.getId(), updateRequest.getEmail());
        }
        if (updateRequest.getDateOfBirth() != null) {
            user.setDateOfBirth(updateRequest.getDateOfBirth());
            logger.debug("User ID {} updating DOB to: {}", user.getId(), updateRequest.getDateOfBirth());
        }
        if (updateRequest.getHeight() != null) {
            user.setHeight(updateRequest.getHeight());
            logger.debug("User ID {} updating height to: {}", user.getId(), updateRequest.getHeight());
        }
        if (updateRequest.getWeight() != null) {
            user.setWeight(updateRequest.getWeight());
            logger.debug("User ID {} updating weight to: {}", user.getId(), updateRequest.getWeight());
        }
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
            logger.debug("User ID {} updating gender to: {}", user.getId(), updateRequest.getGender());
        }

        User updatedUser = userRepository.save(user);
        logger.info("Profile updated successfully for user ID: {}", updatedUser.getId());
        return mapUserToProfileDto(updatedUser);
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            logger.error("Authentication error: No authenticated user found or principal is not UserDetailsImpl.");
            throw new SecurityException("User not authenticated");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> {
                    logger.error("Authenticated user with ID {} not found in database.", userDetails.getId());
                    return new ResourceNotFoundException("User", "id", userDetails.getId());
                });
    }

    private ProfileDto mapUserToProfileDto(User user) {
        return ProfileDto.builder()
                .id(user.getId())
                .identificationNumber(user.getIdentificationNumber())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .height(user.getHeight())
                .weight(user.getWeight())
                .gender(user.getGender())
                .build();
    }
}