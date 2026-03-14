package com.portal.homework.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    public enum Status {
        SUBMITTED,
        GRADED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // assignment relationship
    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    // student relationship
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private String filePath;

    private LocalDateTime submittedAt;

    private Integer grade;

    private String feedback;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Submission() {
        this.submittedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public User getStudent() {
        return student;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Integer getGrade() {
        return grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}