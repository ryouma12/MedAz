package com.example.medaz.dto.record;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AddPrescriptionRequest {

    @NotNull
    private Long clinicId;

    @NotNull
    private LocalDate prescriptionDate;

    @NotEmpty
    @Valid
    private List<AddPrescriptionItemRequest> items;
}
