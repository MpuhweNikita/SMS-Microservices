package com.school.enrollmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Integer id;
    private Integer studentId;
    private Integer courseId;
    private LocalDateTime enrollmentDate;
    private String status;
    private StudentResponse student;
    private CourseResponse course;
}
