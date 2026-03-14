package com.portal.homework.controller;

import com.portal.homework.dto.AssignmentRequest;
import com.portal.homework.dto.AssignmentResponse;
import com.portal.homework.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    /**
     * POST /api/assignments
     * Create a new assignment (TEACHER only)
     */
    @PostMapping
    public ResponseEntity<AssignmentResponse> createAssignment(
            @Valid @RequestBody AssignmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        AssignmentResponse response = assignmentService.createAssignment(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/assignments
     * Retrieve all assignments (authenticated users)
     */
    @GetMapping
    public ResponseEntity<List<AssignmentResponse>> getAllAssignments() {
        return ResponseEntity.ok(assignmentService.getAllAssignments());
    }

    /**
     * GET /api/assignments/{id}
     * Retrieve a specific assignment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> getAssignmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignmentById(id));
    }

    /**
     * GET /api/assignments/my
     * Retrieve all assignments created by the authenticated teacher
     */
    @GetMapping("/my")
    public ResponseEntity<List<AssignmentResponse>> getMyAssignments(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByTeacher(userDetails.getUsername()));
    }
}
