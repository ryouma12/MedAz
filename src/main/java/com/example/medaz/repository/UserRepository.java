package com.example.medaz.repository;

import com.example.medaz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentificationNumber(String identificationNumber);
    Optional<User> findByEmail(String email);
    Boolean existsByIdentificationNumber(String identificationNumber);
    Boolean existsByEmail(String email);
}
