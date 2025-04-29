package com.example.medaz.controller;

import com.example.medaz.dto.profile.ProfileDto;
import com.example.medaz.dto.profile.ProfileUpdateRequest;
import com.example.medaz.service.AuthService;
import com.example.medaz.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
@PreAuthorize("isAuthenticated()")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getCurrentUserProfile() {
        ProfileDto profile = profileService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileDto> updateCurrentUserProfile(@Valid @RequestBody ProfileUpdateRequest updateRequest) {
        ProfileDto updatedProfile = profileService.updateCurrentUserProfile(updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}