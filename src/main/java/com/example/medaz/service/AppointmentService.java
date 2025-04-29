package com.example.medaz.service;

import com.example.medaz.dto.appointment.AppointmentDto;

import java.util.List;

public interface AppointmentService {
    List<AppointmentDto> getUpcomingAppointments();
}
