package com.example.medaz.dto.record;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class VisitDto {
    private Long id;
    private LocalDateTime visitDate;
    private String hospitalName;
    private String hospitalAddress; // Optional
    private String doctorName;
    private String doctorProfession; // Optional
    private Long prescriptionId; // Link to view prescription details
}