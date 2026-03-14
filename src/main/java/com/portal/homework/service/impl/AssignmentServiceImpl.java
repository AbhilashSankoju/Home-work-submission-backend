package com.portal.homework.service.impl;

import com.portal.homework.dto.AssignmentRequest;
import com.portal.homework.dto.AssignmentResponse;
import com.portal.homework.entity.Assignment;
import com.portal.homework.entity.User;
import com.portal.homework.repository.AssignmentRepository;
import com.portal.homework.repository.UserRepository;
import com.portal.homework.service.AssignmentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                                 UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AssignmentResponse createAssignment(AssignmentRequest request, String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found: " + teacherEmail));

        if (teacher.getRole() != User.Role.TEACHER) {
            throw new RuntimeException("Only teachers can create assignments");
        }

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDeadline(request.getDeadline());
        assignment.setTeacher(teacher);

        assignment = assignmentRepository.save(assignment);

        return mapToResponse(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAllAssignments() {

        return assignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentResponse getAssignmentById(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Assignment not found with id: " + id)
                );

        return mapToResponse(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAssignmentsByTeacher(String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() ->
                        new RuntimeException("Teacher not found: " + teacherEmail)
                );

        return assignmentRepository.findByTeacher(teacher)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AssignmentResponse mapToResponse(Assignment assignment) {

        AssignmentResponse response = new AssignmentResponse();

        response.setId(assignment.getId());
        response.setTitle(assignment.getTitle());
        response.setDescription(assignment.getDescription());
        response.setDeadline(assignment.getDeadline());
        response.setTeacherId(assignment.getTeacher().getId());
        response.setTeacherName(assignment.getTeacher().getName());
        response.setCreatedAt(assignment.getCreatedAt());

        return response;
    }
}