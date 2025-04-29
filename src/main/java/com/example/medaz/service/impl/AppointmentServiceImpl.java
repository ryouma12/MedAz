package com.example.medaz.service.impl;

import com.example.medaz.dto.appointment.AppointmentDto;
import com.example.medaz.entity.Appointment;
import com.example.medaz.repository.AppointmentRepository;
import com.example.medaz.security.services.UserDetailsImpl;
import com.example.medaz.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDto> getUpcomingAppointments() {
        Long userId = getCurrentUserId();
        logger.debug("Fetching upcoming appointments for user ID {}", userId);
        List<Appointment> appointments = appointmentRepository.findByUserIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(userId, LocalDateTime.now());

        // Optionally filter further by status if needed (e.g., only SCHEDULED)
        // appointments = appointments.stream()
        //         .filter(a -> a.getStatus() == Appointment.AppointmentStatus.SCHEDULED)
        //         .collect(Collectors.toList());

        return appointments.stream().map(this::mapAppointmentToDto).collect(Collectors.toList());
    }

    // --- Helper Methods ---
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            logger.error("Authentication error: Cannot determine current user ID for appointments.");
            throw new SecurityException("User not authenticated");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    private AppointmentDto mapAppointmentToDto(Appointment appointment) {
        return AppointmentDto.builder()
                .id(appointment.getId())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .hospitalName(appointment.getHospital() != null ? appointment.getHospital().getName() : "N/A")
                .doctorName(appointment.getDoctor() != null ? appointment.getDoctor().getName() : "N/A")
                .doctorProfession(appointment.getDoctor() != null ? appointment.getDoctor().getProfession() : null)
                .status(appointment.getStatus())
                .build();
    }
}
