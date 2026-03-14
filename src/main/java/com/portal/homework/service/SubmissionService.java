package com.portal.homework.service;

import com.portal.homework.dto.SubmissionResponse;
import com.portal.homework.entity.Submission;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SubmissionService {

    SubmissionResponse uploadSubmission(Long assignmentId,
                                         MultipartFile file,
                                         String studentEmail);

    List<SubmissionResponse> getSubmissionsByStudent(Long studentId);

    SubmissionResponse mapToResponse(Submission submission);
}