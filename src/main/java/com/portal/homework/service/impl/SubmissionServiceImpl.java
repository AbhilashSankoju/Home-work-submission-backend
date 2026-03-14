package com.portal.homework.service.impl;

import com.portal.homework.dto.SubmissionResponse;
import com.portal.homework.entity.Assignment;
import com.portal.homework.entity.Submission;
import com.portal.homework.entity.User;
import com.portal.homework.repository.AssignmentRepository;
import com.portal.homework.repository.SubmissionRepository;
import com.portal.homework.repository.UserRepository;
import com.portal.homework.service.SubmissionService;
import com.portal.homework.util.FileStorageUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final FileStorageUtil fileStorageUtil;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 AssignmentRepository assignmentRepository,
                                 UserRepository userRepository,
                                 FileStorageUtil fileStorageUtil) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.fileStorageUtil = fileStorageUtil;
    }

    @Override
    @Transactional
    public SubmissionResponse uploadSubmission(Long assignmentId,
                                               MultipartFile file,
                                               String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() ->
                        new RuntimeException("Student not found: " + studentEmail));

        if (student.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("Only students can submit assignments");
        }

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() ->
                        new RuntimeException("Assignment not found with id: " + assignmentId));

        if (assignment.getDeadline().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Submission deadline has passed");
        }

        submissionRepository.findByAssignmentIdAndStudentId(assignmentId, student.getId())
                .ifPresent(s -> {
                    throw new RuntimeException("You already submitted this assignment");
                });

        String filePath;

        try {
            filePath = fileStorageUtil.storeFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFilePath(filePath);
        submission.setStatus(Submission.Status.SUBMITTED);
        submission.setSubmittedAt(LocalDateTime.now());

        submission = submissionRepository.save(submission);

        return mapToResponse(submission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionsByStudent(Long studentId) {

        return submissionRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubmissionResponse mapToResponse(Submission submission) {

        SubmissionResponse response = new SubmissionResponse();

        response.setId(submission.getId());
        response.setAssignmentId(submission.getAssignment().getId());
        response.setAssignmentTitle(submission.getAssignment().getTitle());
        response.setStudentId(submission.getStudent().getId());
        response.setStudentName(submission.getStudent().getName());
        response.setFilePath(submission.getFilePath());
        response.setSubmittedAt(submission.getSubmittedAt());
        response.setGrade(submission.getGrade());
        response.setFeedback(submission.getFeedback());
        response.setStatus(submission.getStatus());

        return response;
    }
}