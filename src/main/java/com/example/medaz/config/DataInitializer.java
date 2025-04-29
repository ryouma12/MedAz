package com.example.medaz.config;

import com.example.medaz.entity.*;
import com.example.medaz.repository.DoctorRepository;
import com.example.medaz.repository.RoleRepository;
import com.example.medaz.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Checking for initial roles and test users...");

        Role userRole = initializeRole(RoleName.ROLE_USER);
        Role doctorRole = initializeRole(RoleName.ROLE_DOCTOR);

        final String doctorIdentificationNumber = "doctor123";
        final String doctorEmail = "doctor@medaz.local";
        final String doctorPassword = "password123";
        final String doctorFirstName = "Test";
        final String doctorLastName = "Doctor";
        final LocalDate doctorDob = LocalDate.of(1980, 1, 1);
        final Gender doctorGender = Gender.PREFER_NOT_TO_SAY;

        if (!userRepository.existsByEmail(doctorEmail) && !userRepository.existsByIdentificationNumber(doctorIdentificationNumber)) {

            User testDoctorUser = new User();
            testDoctorUser.setIdentificationNumber(doctorIdentificationNumber);
            testDoctorUser.setEmail(doctorEmail);
            testDoctorUser.setPassword(passwordEncoder.encode(doctorPassword));
            testDoctorUser.setFirstName(doctorFirstName);
            testDoctorUser.setLastName(doctorLastName);
            testDoctorUser.setDateOfBirth(doctorDob);
            testDoctorUser.setGender(doctorGender);

            Set<Role> doctorRoles = new HashSet<>();
            if (doctorRole != null) {
                doctorRoles.add(doctorRole);
            } else {
                logger.error("Could not find or create ROLE_DOCTOR. Cannot assign role to test doctor.");
            }
            testDoctorUser.setRoles(doctorRoles);

            User savedUser = userRepository.save(testDoctorUser);
            logger.info("Created test doctor user: {} with ID: {}", doctorEmail, savedUser.getId());

            // Create the corresponding Doctor profile entity - WITHOUT setting ID manually
            // We still check if a doctor record with the *user's* ID already exists,
            // assuming they *should* match, even if the DB generates a different ID for the doctor record itself.
            if (!doctorRepository.existsById(savedUser.getId())) {
                Doctor testDoctorProfile = new Doctor();
                // DO NOT SET ID MANUALLY: testDoctorProfile.setId(savedUser.getId());
                testDoctorProfile.setName(savedUser.getFirstName() + " " + savedUser.getLastName());
                testDoctorProfile.setProfession("General Practice");

                try {
                    Doctor savedDoctor = doctorRepository.save(testDoctorProfile); // Let DB generate Doctor ID
                    logger.info("Created test doctor profile (Doctor ID: {}) potentially corresponding to User ID: {}", savedDoctor.getId(), savedUser.getId());
                    // Important: savedDoctor.getId() might be different from savedUser.getId() now!
                    // The application logic currently *assumes* they are the same via findDoctorByUserIdAssumingIdMatch.
                    // If they differ, the doctor actions will fail later unless you manually ensure IDs match in DB,
                    // or change the lookup logic (Option B).
                } catch (Exception e) {
                    logger.error("Could not create Doctor profile for User ID {}. Error: {}", savedUser.getId(), e.getMessage());
                }

            } else {
                logger.info("Doctor profile with ID {} (matching User ID) already exists.", savedUser.getId());
            }

        } else {
            logger.info("Test doctor user with email {} or ID {} already exists.", doctorEmail, doctorIdentificationNumber);
        }

        logger.info("Data initialization complete.");
    }

    private Role initializeRole(RoleName roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            logger.info("Role {} added to database.", roleName.name());
            return role;
        } else {
            logger.info("Role {} already exists.", roleName.name());
            return roleRepository.findByName(roleName).orElse(null);
        }
    }
}