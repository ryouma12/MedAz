package com.example.medaz.dto.auth;

import com.example.medaz.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String identificationNumber;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Past
    private LocalDate dateOfBirth;

    @PositiveOrZero
    private Double height;

    @PositiveOrZero
    private Double weight;

    @NotNull
    private Gender gender;
}
