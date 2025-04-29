package com.example.medaz.dto.appointment;

import com.example.medaz.entity.Appointment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppointmentDto {
    private Long id;
    private LocalDateTime appointmentDateTime;
    private String hospitalName;
    private String doctorName;
    private String doctorProfession;
    private Appointment.AppointmentStatus status; // Use the enum directly
}

