package com.example.medaz.dto.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDoctorCommentRequest {

    @NotBlank
    @Size(max = 2000)
    private String commentText;
}
