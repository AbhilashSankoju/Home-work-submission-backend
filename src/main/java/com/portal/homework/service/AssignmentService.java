package com.portal.homework.service;

import com.portal.homework.dto.AssignmentRequest;
import com.portal.homework.dto.AssignmentResponse;

import java.util.List;

public interface AssignmentService {

    AssignmentResponse createAssignment(AssignmentRequest request, String teacherEmail);

    List<AssignmentResponse> getAllAssignments();

    AssignmentResponse getAssignmentById(Long id);

    List<AssignmentResponse> getAssignmentsByTeacher(String teacherEmail);
}
