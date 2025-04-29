package com.example.medaz.repository;

import com.example.medaz.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
    List<Diagnosis> findByUserIdOrderByDiagnosisDateDesc(Long userId);
    List<Diagnosis> findByUserIdAndIsChronicOrderByDiagnosisDateDesc(Long userId, Boolean isChronic);
    // Add filtering queries
}
