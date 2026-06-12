package com.school.courseservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {
    @NotBlank(message = "Course code is required")
    private String code;

    @NotBlank(message = "Course title is required")
    private String title;

    private String description;

    @NotNull(message = "Course credits is required")
    @Min(value = 1, message = "Credits must be at least 1")
    private Integer credits;
}
