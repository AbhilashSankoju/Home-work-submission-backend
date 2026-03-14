package com.portal.homework.controller;

import com.portal.homework.dto.GradeRequest;
import com.portal.homework.dto.SubmissionResponse;
import com.portal.homework.service.TeacherReviewService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class TeacherReviewController {

    private final TeacherReviewService teacherReviewService;

    public TeacherReviewController(TeacherReviewService teacherReviewService) {
        this.teacherReviewService = teacherReviewService;
    }

    /**
     * GET /api/submissions/assignment/{assignmentId}
     * Get all submissions for a specific assignment (TEACHER only)
     */
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByAssignment(
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(teacherReviewService.getSubmissionsByAssignment(assignmentId));
    }

    /**
     * GET /api/submissions/view/{submissionId}
     * View / download the uploaded file for a submission (TEACHER only)
     * Returns the file as a downloadable Resource
     */
    @GetMapping("/view/{submissionId}")
    public ResponseEntity<Resource> viewSubmissionFile(@PathVariable Long submissionId) {
        Resource resource = teacherReviewService.getSubmissionFile(submissionId);
        String fileName = teacherReviewService.getSubmissionFileName(submissionId);

        // Determine content type based on file extension
        String contentType = resolveContentType(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

    /**
     * PUT /api/submissions/grade/{submissionId}
     * Grade a submission: assign marks and provide feedback (TEACHER only)
     * Changes submission status from SUBMITTED → GRADED
     */
    @PutMapping("/grade/{submissionId}")
    public ResponseEntity<SubmissionResponse> gradeSubmission(
            @PathVariable Long submissionId,
            @Valid @RequestBody GradeRequest gradeRequest) {

        SubmissionResponse response = teacherReviewService.gradeSubmission(submissionId, gradeRequest);
        return ResponseEntity.ok(response);
    }

    private String resolveContentType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lower.endsWith(".doc")) {
            return "application/msword";
        } else if (lower.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
