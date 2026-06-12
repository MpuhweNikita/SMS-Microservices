package com.school.courseservice.repository;

import com.school.courseservice.entity.Course;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CourseRepository extends ReactiveCrudRepository<Course, Integer> {
    Mono<Course> findByCode(String code);
}
