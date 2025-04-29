package com.example.medaz.service;

import com.example.medaz.dto.profile.ProfileDto;
import com.example.medaz.dto.profile.ProfileUpdateRequest;

public interface ProfileService {
    ProfileDto getCurrentUserProfile();
    ProfileDto updateCurrentUserProfile(ProfileUpdateRequest updateRequest);
}
