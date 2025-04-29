package com.example.medaz.controller;

import com.example.medaz.dto.record.VisitDto;
import com.example.medaz.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/visits")
@PreAuthorize("hasRole('USER')") // Only ROLE_USER for viewing own visits
public class VisitController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public ResponseEntity<List<VisitDto>> getVisits(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String sortBy) {
        List<VisitDto> visits = recordService.getVisits(startDate, endDate, sortBy);
        return ResponseEntity.ok(visits);
    }
}