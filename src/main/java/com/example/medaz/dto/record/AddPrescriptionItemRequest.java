package com.example.medaz.dto.record;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPrescriptionItemRequest {

    @NotBlank
    private String medicationName;

    private String dosage;

    private String instructions;
}
