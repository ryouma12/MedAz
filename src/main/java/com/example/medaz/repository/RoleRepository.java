package com.example.medaz.repository;

import com.example.medaz.entity.Role;
import com.example.medaz.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
    boolean existsByName(RoleName name);
}