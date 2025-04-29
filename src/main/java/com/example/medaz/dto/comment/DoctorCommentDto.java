package com.example.medaz.dto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DoctorCommentDto {
    private Long id;
    private String doctorName;
    private LocalDateTime commentDate;
    private String commentText;
}
