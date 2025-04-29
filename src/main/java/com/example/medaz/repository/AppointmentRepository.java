package com.example.medaz.repository;

import com.example.medaz.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(Long userId, LocalDateTime now);
    List<Appointment> findByUserIdAndStatusOrderByAppointmentDateTimeAsc(Long userId, Appointment.AppointmentStatus status);
}