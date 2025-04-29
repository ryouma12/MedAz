package com.example.medaz.dto.profile;

import com.example.medaz.entity.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ProfileDto {
    private Long id;
    private String identificationNumber;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Double height;
    private Double weight;
    private Gender gender;
}
