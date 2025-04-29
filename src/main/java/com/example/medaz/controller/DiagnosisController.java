package com.example.medaz.controller;

import com.example.medaz.dto.record.DiagnosisDto;
import com.example.medaz.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/diagnoses")
@PreAuthorize("hasRole('USER')") // Only ROLE_USER for viewing own diagnoses
public class DiagnosisController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public ResponseEntity<List<DiagnosisDto>> getDiagnoses(
            @RequestParam(required = false) Boolean isChronic) {
        List<DiagnosisDto> diagnoses = recordService.getDiagnoses(isChronic);
        return ResponseEntity.ok(diagnoses);
    }
}
