package com.school.enrollmentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentRequest {
    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    private String status;
}
