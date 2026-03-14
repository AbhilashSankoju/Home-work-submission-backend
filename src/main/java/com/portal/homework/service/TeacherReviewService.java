package com.portal.homework.service;

import com.portal.homework.dto.GradeRequest;
import com.portal.homework.dto.SubmissionResponse;
import org.springframework.core.io.Resource;

import java.util.List;

public interface TeacherReviewService {

    List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId);

    Resource getSubmissionFile(Long submissionId);

    String getSubmissionFileName(Long submissionId);

    SubmissionResponse gradeSubmission(Long submissionId, GradeRequest gradeRequest);
}
