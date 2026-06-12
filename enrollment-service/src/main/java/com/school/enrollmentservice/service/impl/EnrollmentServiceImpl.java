package com.school.enrollmentservice.service.impl;

import com.school.enrollmentservice.client.CourseClient;
import com.school.enrollmentservice.client.StudentClient;
import com.school.enrollmentservice.dto.CourseResponse;
import com.school.enrollmentservice.dto.EnrollmentRequest;
import com.school.enrollmentservice.dto.EnrollmentResponse;
import com.school.enrollmentservice.dto.StudentResponse;
import com.school.enrollmentservice.entity.Enrollment;
import com.school.enrollmentservice.exception.DuplicateResourceException;
import com.school.enrollmentservice.exception.ResourceNotFoundException;
import com.school.enrollmentservice.mapper.EnrollmentMapper;
import com.school.enrollmentservice.repository.EnrollmentRepository;
import com.school.enrollmentservice.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final StudentClient studentClient;
    private final CourseClient courseClient;

    @Override
    @Transactional
    public Mono<EnrollmentResponse> createEnrollment(EnrollmentRequest request) {
        log.info("Enrolling student {} in course {}", request.getStudentId(), request.getCourseId());

        // Step 1: Validate Student existence
        return studentClient.getStudentById(request.getStudentId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student with ID " + request.getStudentId() + " does not exist")))
                .flatMap(student ->
                        // Step 2: Validate Course existence
                        courseClient.getCourseById(request.getCourseId())
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Course with ID " + request.getCourseId() + " does not exist")))
                                .flatMap(course ->
                                        // Step 3: Check if already enrolled
                                        enrollmentRepository.findByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())
                                                .flatMap(existing -> Mono.<EnrollmentResponse>error(new DuplicateResourceException("Student is already enrolled in this course")))
                                                .switchIfEmpty(Mono.defer(() ->
                                                        // Step 4: Save enrollment
                                                        enrollmentRepository.save(enrollmentMapper.toEntity(request))
                                                                .map(enrollment -> {
                                                                        EnrollmentResponse response = enrollmentMapper.toResponse(enrollment);
                                                                        response.setStudent(student);
                                                                        response.setCourse(course);
                                                                        return response;
                                                                })
                                                ))
                                )
                );
    }

    @Override
    public Flux<EnrollmentResponse> getAllEnrollments() {
        log.info("Fetching all enrollments");
        return enrollmentRepository.findAll()
                .flatMap(this::populateEnrollmentDetails);
    }

    @Override
    public Mono<EnrollmentResponse> getEnrollmentById(Integer id) {
        log.info("Fetching enrollment by id: {}", id);
        return enrollmentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Enrollment with id " + id + " not found")))
                .flatMap(this::populateEnrollmentDetails);
    }

    private Mono<EnrollmentResponse> populateEnrollmentDetails(Enrollment enrollment) {
        Mono<StudentResponse> studentMono = studentClient.getStudentById(enrollment.getStudentId())
                .defaultIfEmpty(StudentResponse.builder()
                        .id(enrollment.getStudentId())
                        .firstName("Unknown")
                        .lastName("Student")
                        .build());

        Mono<CourseResponse> courseMono = courseClient.getCourseById(enrollment.getCourseId())
                .defaultIfEmpty(CourseResponse.builder()
                        .id(enrollment.getCourseId())
                        .title("Unknown Course")
                        .build());

        // Perform parallel reactive service calls via Mono.zip
        return Mono.zip(studentMono, courseMono)
                .map(tuple -> {
                    EnrollmentResponse response = enrollmentMapper.toResponse(enrollment);
                    response.setStudent(tuple.getT1());
                    response.setCourse(tuple.getT2());
                    return response;
                });
    }
}
