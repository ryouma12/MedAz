package com.example.medaz.dto.profile;

import com.example.medaz.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileUpdateRequest {
    @Email
    @Size(max = 50)
    private String email;

    @Past
    private LocalDate dateOfBirth;

    @PositiveOrZero
    private Double height;

    @PositiveOrZero
    private Double weight;

    private Gender gender;
}