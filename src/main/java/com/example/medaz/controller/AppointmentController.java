package com.example.medaz.controller;

import com.example.medaz.dto.appointment.AppointmentDto;
import com.example.medaz.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appointments")
@PreAuthorize("hasRole('USER')") // Only ROLE_USER for viewing own upcoming appointments
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentDto>> getUpcomingAppointments() {
        List<AppointmentDto> appointments = appointmentService.getUpcomingAppointments();
        return ResponseEntity.ok(appointments);
    }
}
