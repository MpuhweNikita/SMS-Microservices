package com.school.enrollmentservice.mapper;

import com.school.enrollmentservice.dto.EnrollmentRequest;
import com.school.enrollmentservice.dto.EnrollmentResponse;
import com.school.enrollmentservice.entity.Enrollment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EnrollmentMapper {

    public Enrollment toEntity(EnrollmentRequest request) {
        if (request == null) {
            return null;
        }
        return Enrollment.builder()
                .studentId(request.getStudentId())
                .courseId(request.getCourseId())
                .enrollmentDate(LocalDateTime.now())
                .status(request.getStatus() != null ? request.getStatus() : "ENROLLED")
                .build();
    }

    public EnrollmentResponse toResponse(Enrollment entity) {
        if (entity == null) {
            return null;
        }
        return EnrollmentResponse.builder()
                .id(entity.getId())
                .studentId(entity.getStudentId())
                .courseId(entity.getCourseId())
                .enrollmentDate(entity.getEnrollmentDate())
                .status(entity.getStatus())
                .build();
    }
}
