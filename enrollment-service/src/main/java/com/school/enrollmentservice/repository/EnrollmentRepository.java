package com.school.enrollmentservice.repository;

import com.school.enrollmentservice.entity.Enrollment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EnrollmentRepository extends ReactiveCrudRepository<Enrollment, Integer> {
    Mono<Enrollment> findByStudentIdAndCourseId(Integer studentId, Integer courseId);
}
