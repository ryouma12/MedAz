package com.example.medaz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_comments")
@Getter
@Setter
@NoArgsConstructor
public class DoctorComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // The patient the comment is for
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id") // The doctor who made the comment
    private Doctor doctor;

    @Column(nullable = false, length = 2000) // Or use @Lob for very long text
    private String commentText;

    @Column(nullable = false)
    private LocalDateTime commentDate;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        commentDate = LocalDateTime.now(); // Set comment date on creation
    }
}