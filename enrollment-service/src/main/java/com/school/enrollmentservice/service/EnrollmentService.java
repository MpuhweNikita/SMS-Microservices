package com.school.enrollmentservice.service;

import com.school.enrollmentservice.dto.EnrollmentRequest;
import com.school.enrollmentservice.dto.EnrollmentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EnrollmentService {
    Mono<EnrollmentResponse> createEnrollment(EnrollmentRequest request);
    Flux<EnrollmentResponse> getAllEnrollments();
    Mono<EnrollmentResponse> getEnrollmentById(Integer id);
}
