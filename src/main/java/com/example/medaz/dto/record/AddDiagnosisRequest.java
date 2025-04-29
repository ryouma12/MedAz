package com.example.medaz.dto.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AddDiagnosisRequest {

    @NotBlank
    private String diagnosisName;

    @NotNull
    private Long clinicId;

    @NotNull
    private LocalDate diagnosisDate;

    @NotNull
    private Boolean isChronic = false;
}