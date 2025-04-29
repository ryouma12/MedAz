package com.example.medaz.service.impl;


import com.example.medaz.dto.comment.DoctorCommentDto;
import com.example.medaz.dto.record.*;
import com.example.medaz.entity.*;
import com.example.medaz.exception.ResourceNotFoundException;
import com.example.medaz.repository.*;
import com.example.medaz.security.services.UserDetailsImpl;
import com.example.medaz.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl implements RecordService {

    private static final Logger logger = LoggerFactory.getLogger(RecordServiceImpl.class);

    @Autowired private VisitRepository visitRepository;
    @Autowired private TestResultRepository testResultRepository;
    @Autowired private DiagnosisRepository diagnosisRepository;
    @Autowired private PrescriptionRepository prescriptionRepository;
    @Autowired private DoctorCommentRepository doctorCommentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private HospitalRepository hospitalRepository;
    @Autowired private DoctorRepository doctorRepository;

    // --- USER methods ---
    @Override
    @Transactional(readOnly = true)
    public List<VisitDto> getVisits(LocalDate startDate, LocalDate endDate, String sortBy) {
        Long userId = getCurrentUserId();
        logger.debug("Fetching visits for user ID {} between {} and {}", userId, startDate, endDate);
        List<Visit> visits;
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            visits = visitRepository.findByUserIdAndVisitDateBetweenOrderByVisitDateDesc(userId, startDateTime, endDateTime);
        } else {
            visits = visitRepository.findByUserIdOrderByVisitDateDesc(userId);
        }
        return visits.stream().map(this::mapVisitToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestResultDto> getTestResults() {
        Long userId = getCurrentUserId();
        logger.debug("Fetching test results for user ID {}", userId);
        List<TestResult> results = testResultRepository.findByUserIdOrderByTestDateDesc(userId);
        return results.stream().map(this::mapTestResultToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosisDto> getDiagnoses(Boolean isChronic) {
        Long userId = getCurrentUserId();
        logger.debug("Fetching diagnoses for user ID {}, chronic filter: {}", userId, isChronic);
        List<Diagnosis> diagnoses;
        if (isChronic != null) {
            diagnoses = diagnosisRepository.findByUserIdAndIsChronicOrderByDiagnosisDateDesc(userId, isChronic);
        } else {
            diagnoses = diagnosisRepository.findByUserIdOrderByDiagnosisDateDesc(userId);
        }
        return diagnoses.stream().map(this::mapDiagnosisToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDto> getPrescriptions() {
        Long userId = getCurrentUserId();
        logger.debug("Fetching prescription list for user ID {}", userId);
        List<Prescription> prescriptions = prescriptionRepository.findByUserIdOrderByPrescriptionDateDesc(userId);
        return prescriptions.stream().map(p -> mapPrescriptionToDto(p, false)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionDto getPrescriptionDetails(Long prescriptionId) {
        Long userId = getCurrentUserId();
        logger.debug("Fetching details for prescription ID {} for user ID {}", prescriptionId, userId);
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", "id", prescriptionId));
        if (!prescription.getUser().getId().equals(userId)) {
            logger.warn("Security violation: User {} attempted to access prescription {}", userId, prescriptionId);
            throw new SecurityException("Access denied to this prescription");
        }
        return mapPrescriptionToDto(prescription, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorCommentDto> getDoctorComments() {
        Long userId = getCurrentUserId();
        logger.debug("Fetching doctor comments for user ID {}", userId);
        List<DoctorComment> comments = doctorCommentRepository.findByUserIdOrderByCommentDateDesc(userId);
        return comments.stream().map(this::mapDoctorCommentToDto).collect(Collectors.toList());
    }


    // --- DOCTOR view methods ---
    @Override
    @Transactional(readOnly = true)
    public List<VisitDto> getVisitsForPatient(Long patientId, LocalDate startDate, LocalDate endDate, String sortBy) {
        checkPatientExists(patientId);
        logger.debug("Fetching visits for specific patient ID {} between {} and {}", patientId, startDate, endDate);
        List<Visit> visits;
        if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
            visits = visitRepository.findByUserIdAndVisitDateBetweenOrderByVisitDateDesc(patientId, startDateTime, endDateTime);
        } else {
            visits = visitRepository.findByUserIdOrderByVisitDateDesc(patientId);
        }
        return visits.stream().map(this::mapVisitToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestResultDto> getTestResultsForPatient(Long patientId) {
        checkPatientExists(patientId);
        logger.debug("Fetching test results for specific patient ID {}", patientId);
        List<TestResult> results = testResultRepository.findByUserIdOrderByTestDateDesc(patientId);
        return results.stream().map(this::mapTestResultToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiagnosisDto> getDiagnosesForPatient(Long patientId, Boolean isChronic) {
        checkPatientExists(patientId);
        logger.debug("Fetching diagnoses for specific patient ID {}, chronic filter: {}", patientId, isChronic);
        List<Diagnosis> diagnoses;
        if (isChronic != null) {
            diagnoses = diagnosisRepository.findByUserIdAndIsChronicOrderByDiagnosisDateDesc(patientId, isChronic);
        } else {
            diagnoses = diagnosisRepository.findByUserIdOrderByDiagnosisDateDesc(patientId);
        }
        return diagnoses.stream().map(this::mapDiagnosisToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionDto> getPrescriptionsForPatient(Long patientId) {
        checkPatientExists(patientId);
        logger.debug("Fetching prescription list for specific patient ID {}", patientId);
        List<Prescription> prescriptions = prescriptionRepository.findByUserIdOrderByPrescriptionDateDesc(patientId);
        return prescriptions.stream().map(p -> mapPrescriptionToDto(p, false)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PrescriptionDto getPrescriptionDetailsForPatient(Long patientId, Long prescriptionId) {
        checkPatientExists(patientId);
        logger.debug("Fetching details for prescription ID {} for specific patient ID {}", prescriptionId, patientId);
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription", "id", prescriptionId));
        if (!prescription.getUser().getId().equals(patientId)) {
            logger.warn("Data mismatch: Doctor requested prescription {} for patient {}, but it belongs to patient {}", prescriptionId, patientId, prescription.getUser().getId());
            throw new ResourceNotFoundException("Prescription", "id", prescriptionId);
        }
        return mapPrescriptionToDto(prescription, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorCommentDto> getDoctorCommentsForPatient(Long patientId) {
        checkPatientExists(patientId);
        logger.debug("Fetching doctor comments for specific patient ID {}", patientId);
        List<DoctorComment> comments = doctorCommentRepository.findByUserIdOrderByCommentDateDesc(patientId);
        return comments.stream().map(this::mapDoctorCommentToDto).collect(Collectors.toList());
    }


    // --- DOCTOR create methods ---

    @Override
    @Transactional
    public TestResultDto addTestResultForPatient(Long patientId, AddTestResultRequest request) {
        User patient = findPatientById(patientId);
        User doctorUser = getCurrentAuthenticatedUser();
        // Use the correct helper method name
        Doctor doctor = findDoctorByUserIdAssumingIdMatch(doctorUser.getId()); // <<< RENAMED CALL
        Hospital clinic = findClinicById(request.getClinicId());

        TestResult testResult = new TestResult();
        testResult.setUser(patient);
        testResult.setTestName(request.getTestName());
        testResult.setResult(request.getResult());
        testResult.setClinic(clinic);
        testResult.setDoctor(doctor);
        testResult.setTestDate(request.getTestDate());

        TestResult savedResult = testResultRepository.save(testResult);
        logger.info("Doctor User ID {} (Doctor ID {}) added test result ID {} for patient ID {}", doctorUser.getId(), doctor.getId(), savedResult.getId(), patientId);
        return mapTestResultToDto(savedResult);
    }

    @Override
    @Transactional
    public DiagnosisDto addDiagnosisForPatient(Long patientId, AddDiagnosisRequest request) {
        User patient = findPatientById(patientId);
        User doctorUser = getCurrentAuthenticatedUser();
        // Use the correct helper method name
        Doctor doctor = findDoctorByUserIdAssumingIdMatch(doctorUser.getId()); // <<< RENAMED CALL
        Hospital clinic = findClinicById(request.getClinicId());

        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setUser(patient);
        diagnosis.setDiagnosisName(request.getDiagnosisName());
        diagnosis.setClinic(clinic);
        diagnosis.setDoctor(doctor);
        diagnosis.setDiagnosisDate(request.getDiagnosisDate());
        diagnosis.setIsChronic(request.getIsChronic());

        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);
        logger.info("Doctor User ID {} (Doctor ID {}) added diagnosis ID {} for patient ID {}", doctorUser.getId(), doctor.getId(), savedDiagnosis.getId(), patientId);
        return mapDiagnosisToDto(savedDiagnosis);
    }

    @Override
    @Transactional
    public PrescriptionDto addPrescriptionForPatient(Long patientId, AddPrescriptionRequest request) {
        User patient = findPatientById(patientId);
        User doctorUser = getCurrentAuthenticatedUser();
        // Use the correct helper method name
        Doctor doctor = findDoctorByUserIdAssumingIdMatch(doctorUser.getId()); // <<< RENAMED CALL
        Hospital clinic = findClinicById(request.getClinicId());

        Prescription prescription = new Prescription();
        prescription.setUser(patient);
        prescription.setClinic(clinic);
        prescription.setDoctor(doctor);
        prescription.setPrescriptionDate(request.getPrescriptionDate());

        for (AddPrescriptionItemRequest itemRequest : request.getItems()) {
            PrescriptionItem item = new PrescriptionItem();
            item.setMedicationName(itemRequest.getMedicationName());
            item.setDosage(itemRequest.getDosage());
            item.setInstructions(itemRequest.getInstructions());
            prescription.addItem(item);
        }

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        logger.info("Doctor User ID {} (Doctor ID {}) added prescription ID {} for patient ID {}", doctorUser.getId(), doctor.getId(), savedPrescription.getId(), patientId);
        return mapPrescriptionToDto(savedPrescription, true);
    }

    @Override
    @Transactional
    public DoctorCommentDto addDoctorCommentForPatient(Long patientId, AddDoctorCommentRequest request) {
        User patient = findPatientById(patientId);
        User doctorUser = getCurrentAuthenticatedUser();
        // Use the correct helper method name
        Doctor doctor = findDoctorByUserIdAssumingIdMatch(doctorUser.getId()); // <<< RENAMED CALL

        DoctorComment comment = new DoctorComment();
        comment.setUser(patient);
        comment.setDoctor(doctor);
        comment.setCommentText(request.getCommentText());

        DoctorComment savedComment = doctorCommentRepository.save(comment);
        logger.info("Doctor User ID {} (Doctor ID {}) added comment ID {} for patient ID {}", doctorUser.getId(), doctor.getId(), savedComment.getId(), patientId);
        return mapDoctorCommentToDto(savedComment);
    }


    // --- Helper Methods ---

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            logger.error("Authentication error: Cannot determine current user ID.");
            throw new SecurityException("User not authenticated");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    private User getCurrentAuthenticatedUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Authenticated user with ID {} not found in database.", userId);
                    return new ResourceNotFoundException("User", "id", userId);
                });
    }

    private void checkPatientExists(Long patientId) {
        if (!userRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient", "id", patientId);
        }
    }

    private User findPatientById(Long patientId) {
        return userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));
    }

    private Hospital findClinicById(Long clinicId) {
        return hospitalRepository.findById(clinicId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic/Hospital", "id", clinicId));
    }

    // Renamed and modified lookup method
    private Doctor findDoctorByUserIdAssumingIdMatch(Long userId) {
        return doctorRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile matching user ID", "id", userId));
    }


    // --- Mapping Methods (Entity to DTO) ---
    private VisitDto mapVisitToDto(Visit visit) {
        return VisitDto.builder()
                .id(visit.getId())
                .visitDate(visit.getVisitDate())
                .hospitalName(visit.getHospital() != null ? visit.getHospital().getName() : "N/A")
                .hospitalAddress(visit.getHospital() != null ? visit.getHospital().getAddress() : null)
                .doctorName(visit.getDoctor() != null ? visit.getDoctor().getName() : "N/A")
                .doctorProfession(visit.getDoctor() != null ? visit.getDoctor().getProfession() : null)
                .prescriptionId(visit.getPrescription() != null ? visit.getPrescription().getId() : null)
                .build();
    }

    private TestResultDto mapTestResultToDto(TestResult testResult) {
        return TestResultDto.builder()
                .id(testResult.getId())
                .testDate(testResult.getTestDate())
                .testName(testResult.getTestName())
                .result(testResult.getResult())
                .clinicName(testResult.getClinic() != null ? testResult.getClinic().getName() : "N/A")
                .doctorName(testResult.getDoctor() != null ? testResult.getDoctor().getName() : "N/A")
                .build();
    }

    private DiagnosisDto mapDiagnosisToDto(Diagnosis diagnosis) {
        return DiagnosisDto.builder()
                .id(diagnosis.getId())
                .diagnosisDate(diagnosis.getDiagnosisDate())
                .diagnosisName(diagnosis.getDiagnosisName())
                .clinicName(diagnosis.getClinic() != null ? diagnosis.getClinic().getName() : "N/A")
                .doctorName(diagnosis.getDoctor() != null ? diagnosis.getDoctor().getName() : "N/A")
                .isChronic(diagnosis.getIsChronic())
                .build();
    }

    private PrescriptionDto mapPrescriptionToDto(Prescription prescription, boolean includeItems) {
        List<PrescriptionItemDto> itemDtos = includeItems && prescription.getItems() != null ?
                prescription.getItems().stream().map(this::mapPrescriptionItemToDto).collect(Collectors.toList())
                : Collections.emptyList();

        return PrescriptionDto.builder()
                .id(prescription.getId())
                .prescriptionDate(prescription.getPrescriptionDate())
                .clinicName(prescription.getClinic() != null ? prescription.getClinic().getName() : "N/A")
                .doctorName(prescription.getDoctor() != null ? prescription.getDoctor().getName() : "N/A")
                .items(itemDtos)
                .build();
    }

    private PrescriptionItemDto mapPrescriptionItemToDto(PrescriptionItem item) {
        return PrescriptionItemDto.builder()
                .id(item.getId())
                .medicationName(item.getMedicationName())
                .dosage(item.getDosage())
                .instructions(item.getInstructions())
                .build();
    }

    private DoctorCommentDto mapDoctorCommentToDto(DoctorComment comment) {
        return DoctorCommentDto.builder()
                .id(comment.getId())
                .doctorName(comment.getDoctor() != null ? comment.getDoctor().getName() : "System")
                .commentDate(comment.getCommentDate())
                .commentText(comment.getCommentText())
                .build();
    }
}