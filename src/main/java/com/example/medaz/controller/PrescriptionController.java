package com.example.medaz.controller;

import com.example.medaz.dto.record.PrescriptionDto;
import com.example.medaz.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/prescriptions")
@PreAuthorize("hasRole('USER')") // Only ROLE_USER for viewing own prescriptions
public class PrescriptionController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> getPrescriptions() {
        List<PrescriptionDto> prescriptions = recordService.getPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDto> getPrescriptionDetails(@PathVariable Long id) {
        PrescriptionDto prescription = recordService.getPrescriptionDetails(id);
        return ResponseEntity.ok(prescription);
    }
}
