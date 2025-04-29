package com.example.medaz.dto.record;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DiagnosisDto {
    private Long id;
    private LocalDate diagnosisDate;
    private String diagnosisName;
    private String clinicName;
    private String doctorName;
    private Boolean isChronic;
}
