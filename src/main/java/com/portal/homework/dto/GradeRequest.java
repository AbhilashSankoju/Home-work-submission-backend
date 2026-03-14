package com.portal.homework.dto;

public class GradeRequest {

    private Integer grade;
    private String feedback;

    public GradeRequest() {
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}