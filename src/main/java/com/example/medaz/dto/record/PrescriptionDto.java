package com.example.medaz.dto.record;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class PrescriptionDto {
    private Long id;
    private LocalDate prescriptionDate;
    private String clinicName;
    private String doctorName;
    private List<PrescriptionItemDto> items; // Include items in detailed view
}
