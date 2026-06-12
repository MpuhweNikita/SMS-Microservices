package com.school.enrollmentservice.controller;

import com.school.enrollmentservice.dto.EnrollmentRequest;
import com.school.enrollmentservice.dto.EnrollmentResponse;
import com.school.enrollmentservice.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public Mono<ResponseEntity<EnrollmentResponse>> createEnrollment(@Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.createEnrollment(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping
    public Flux<EnrollmentResponse> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EnrollmentResponse>> getEnrollmentById(@PathVariable Integer id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok);
    }
}
