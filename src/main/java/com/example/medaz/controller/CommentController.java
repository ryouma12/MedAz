package com.example.medaz.controller;

import com.example.medaz.dto.comment.DoctorCommentDto;
import com.example.medaz.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;




@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comments")
@PreAuthorize("hasRole('USER')") // Only ROLE_USER for viewing comments left for them
public class CommentController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/from-doctors")
    public ResponseEntity<List<DoctorCommentDto>> getDoctorComments() {
        List<DoctorCommentDto> comments = recordService.getDoctorComments();
        return ResponseEntity.ok(comments);
    }
}
