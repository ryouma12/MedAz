package com.example.medaz.controller;

import com.example.medaz.dto.record.TestResultDto;
import com.example.medaz.service.RecordService;
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
@RequestMapping("/api/tests")
@PreAuthorize("hasRole('USER')") // Only ROLE_USER for viewing own test results
public class TestResultController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public ResponseEntity<List<TestResultDto>> getTestResults() {
        List<TestResultDto> results = recordService.getTestResults();
        return ResponseEntity.ok(results);
    }
}
