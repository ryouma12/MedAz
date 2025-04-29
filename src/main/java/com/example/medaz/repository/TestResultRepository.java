package com.example.medaz.repository;

import com.example.medaz.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUserIdOrderByTestDateDesc(Long userId);
}