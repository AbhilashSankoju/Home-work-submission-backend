package com.portal.homework.service.impl;

import com.portal.homework.dto.GradeRequest;
import com.portal.homework.dto.SubmissionResponse;
import com.portal.homework.entity.Submission;
import com.portal.homework.repository.SubmissionRepository;
import com.portal.homework.service.TeacherReviewService;
import com.portal.homework.service.SubmissionService;
import com.portal.homework.util.FileStorageUtil;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherReviewServiceImpl implements TeacherReviewService {

    private final SubmissionRepository submissionRepository;
    private final FileStorageUtil fileStorageUtil;
    private final SubmissionService submissionService;

    public TeacherReviewServiceImpl(SubmissionRepository submissionRepository,
                                    FileStorageUtil fileStorageUtil,
                                    SubmissionService submissionService) {
        this.submissionRepository = submissionRepository;
        this.fileStorageUtil = fileStorageUtil;
        this.submissionService = submissionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId) {

        return submissionRepository.findByAssignmentId(assignmentId)
                .stream()
                .map(submissionService::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource getSubmissionFile(Long submissionId) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() ->
                        new RuntimeException("Submission not found with id: " + submissionId));

        try {

            Path filePath = fileStorageUtil.getFilePath(submission.getFilePath());

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            throw new RuntimeException("File not readable: " + submission.getFilePath());

        } catch (MalformedURLException e) {
            throw new RuntimeException("File path error: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getSubmissionFileName(Long submissionId) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() ->
                        new RuntimeException("Submission not found with id: " + submissionId));

        Path path = Path.of(submission.getFilePath());

        return path.getFileName().toString();
    }

    @Override
    @Transactional
    public SubmissionResponse gradeSubmission(Long submissionId, GradeRequest request) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() ->
                        new RuntimeException("Submission not found with id: " + submissionId));

        submission.setGrade(request.getGrade());
        submission.setFeedback(request.getFeedback());
        submission.setStatus(Submission.Status.GRADED);

        submission = submissionRepository.save(submission);

        return submissionService.mapToResponse(submission);
    }
}