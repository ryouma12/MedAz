package com.example.medaz.repository;

import com.example.medaz.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByUserIdOrderByVisitDateDesc(Long userId);
    List<Visit> findByUserIdAndVisitDateBetweenOrderByVisitDateDesc(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}