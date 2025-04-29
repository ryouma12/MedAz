package com.example.medaz.dto.record;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TestResultDto {
    private Long id;
    private LocalDate testDate;
    private String testName;
    private String result;
    private String clinicName;
    private String doctorName;
}

