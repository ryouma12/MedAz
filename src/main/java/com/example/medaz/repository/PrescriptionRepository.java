package com.example.medaz.repository;

import com.example.medaz.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByUserIdOrderByPrescriptionDateDesc(Long userId);
}
