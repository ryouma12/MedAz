package com.example.medaz.controller;

import com.example.medaz.dto.comment.DoctorCommentDto;
import com.example.medaz.dto.record.*;
import com.example.medaz.service.RecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/doctor/patients/{patientId}")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorRecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/visits")
    public ResponseEntity<List<VisitDto>> getPatientVisits(
            @PathVariable Long patientId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String sortBy) {
        List<VisitDto> visits = recordService.getVisitsForPatient(patientId, startDate, endDate, sortBy);
        return ResponseEntity.ok(visits);
    }

    @GetMapping("/tests")
    public ResponseEntity<List<TestResultDto>> getPatientTestResults(@PathVariable Long patientId) {
        List<TestResultDto> results = recordService.getTestResultsForPatient(patientId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/diagnoses")
    public ResponseEntity<List<DiagnosisDto>> getPatientDiagnoses(
            @PathVariable Long patientId,
            @RequestParam(required = false) Boolean isChronic) {
        List<DiagnosisDto> diagnoses = recordService.getDiagnosesForPatient(patientId, isChronic);
        return ResponseEntity.ok(diagnoses);
    }

    @GetMapping("/prescriptions")
    public ResponseEntity<List<PrescriptionDto>> getPatientPrescriptions(@PathVariable Long patientId) {
        List<PrescriptionDto> prescriptions = recordService.getPrescriptionsForPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/prescriptions/{prescriptionId}")
    public ResponseEntity<PrescriptionDto> getPatientPrescriptionDetails(
            @PathVariable Long patientId,
            @PathVariable Long prescriptionId) {
        PrescriptionDto prescription = recordService.getPrescriptionDetailsForPatient(patientId, prescriptionId);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<DoctorCommentDto>> getPatientDoctorComments(@PathVariable Long patientId) {
        List<DoctorCommentDto> comments = recordService.getDoctorCommentsForPatient(patientId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/tests")
    public ResponseEntity<TestResultDto> addTestResult(
            @PathVariable Long patientId,
            @Valid @RequestBody AddTestResultRequest request) {
        TestResultDto createdResult = recordService.addTestResultForPatient(patientId, request);
        return new ResponseEntity<>(createdResult, HttpStatus.CREATED);
    }

    @PostMapping("/diagnoses")
    public ResponseEntity<DiagnosisDto> addDiagnosis(
            @PathVariable Long patientId,
            @Valid @RequestBody AddDiagnosisRequest request) {
        DiagnosisDto createdDiagnosis = recordService.addDiagnosisForPatient(patientId, request);
        return new ResponseEntity<>(createdDiagnosis, HttpStatus.CREATED);
    }

    @PostMapping("/prescriptions")
    public ResponseEntity<PrescriptionDto> addPrescription(
            @PathVariable Long patientId,
            @Valid @RequestBody AddPrescriptionRequest request) {
        PrescriptionDto createdPrescription = recordService.addPrescriptionForPatient(patientId, request);
        return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
    }

    @PostMapping("/comments")
    public ResponseEntity<DoctorCommentDto> addDoctorComment(
            @PathVariable Long patientId,
            @Valid @RequestBody AddDoctorCommentRequest request) {
        DoctorCommentDto createdComment = recordService.addDoctorCommentForPatient(patientId, request);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}