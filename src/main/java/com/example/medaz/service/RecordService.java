package com.example.medaz.service;

import com.example.medaz.dto.comment.DoctorCommentDto;
import com.example.medaz.dto.record.*;

import java.time.LocalDate;
import java.util.List;

public interface RecordService {
    List<VisitDto> getVisits(LocalDate startDate, LocalDate endDate, String sortBy);
    List<TestResultDto> getTestResults();
    List<DiagnosisDto> getDiagnoses(Boolean isChronic);
    List<PrescriptionDto> getPrescriptions();
    PrescriptionDto getPrescriptionDetails(Long prescriptionId);
    List<DoctorCommentDto> getDoctorComments();

    List<VisitDto> getVisitsForPatient(Long patientId, LocalDate startDate, LocalDate endDate, String sortBy);
    List<TestResultDto> getTestResultsForPatient(Long patientId);
    List<DiagnosisDto> getDiagnosesForPatient(Long patientId, Boolean isChronic);
    List<PrescriptionDto> getPrescriptionsForPatient(Long patientId);
    PrescriptionDto getPrescriptionDetailsForPatient(Long patientId, Long prescriptionId);
    List<DoctorCommentDto> getDoctorCommentsForPatient(Long patientId);

    TestResultDto addTestResultForPatient(Long patientId, AddTestResultRequest request);
    DiagnosisDto addDiagnosisForPatient(Long patientId, AddDiagnosisRequest request);
    PrescriptionDto addPrescriptionForPatient(Long patientId, AddPrescriptionRequest request);
    DoctorCommentDto addDoctorCommentForPatient(Long patientId, AddDoctorCommentRequest request);

}