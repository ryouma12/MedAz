package com.example.medaz.repository;

import com.example.medaz.entity.DoctorComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorCommentRepository extends JpaRepository<DoctorComment, Long> {
    List<DoctorComment> findByUserIdOrderByCommentDateDesc(Long userId);
    // Add filtering queries
}
