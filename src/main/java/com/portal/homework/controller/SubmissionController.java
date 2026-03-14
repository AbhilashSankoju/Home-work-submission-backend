package com.portal.homework.controller;

import com.portal.homework.dto.SubmissionResponse;
import com.portal.homework.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /**
     * POST /api/submissions/upload
     * Upload a homework file for an assignment (STUDENT only)
     * Accepts multipart/form-data with 'assignmentId' and 'file' fields
     */
    @PostMapping("/upload")
    public ResponseEntity<SubmissionResponse> uploadSubmission(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        SubmissionResponse response = submissionService.uploadSubmission(
                assignmentId, file, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/submissions/student/{studentId}
     * Get all submissions made by a specific student (STUDENT only)
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByStudent(studentId));
    }
}
