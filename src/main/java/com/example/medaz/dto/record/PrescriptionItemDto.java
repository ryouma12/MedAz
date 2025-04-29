package com.example.medaz.dto.record;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PrescriptionItemDto {
    private Long id;
    private String medicationName;
    private String dosage;
    private String instructions;
}