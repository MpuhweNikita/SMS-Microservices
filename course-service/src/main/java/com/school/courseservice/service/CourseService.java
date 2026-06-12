package com.school.courseservice.service;

import com.school.courseservice.dto.CourseRequest;
import com.school.courseservice.dto.CourseResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {
    Mono<CourseResponse> createCourse(CourseRequest request);
    Flux<CourseResponse> getAllCourses();
    Mono<CourseResponse> getCourseById(Integer id);
    Mono<CourseResponse> updateCourse(Integer id, CourseRequest request);
    Mono<Void> deleteCourse(Integer id);
}
