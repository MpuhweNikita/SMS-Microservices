package com.school.courseservice.service.impl;

import com.school.courseservice.dto.CourseRequest;
import com.school.courseservice.dto.CourseResponse;
import com.school.courseservice.entity.Course;
import com.school.courseservice.exception.DuplicateResourceException;
import com.school.courseservice.exception.ResourceNotFoundException;
import com.school.courseservice.mapper.CourseMapper;
import com.school.courseservice.repository.CourseRepository;
import com.school.courseservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public Mono<CourseResponse> createCourse(CourseRequest request) {
        log.info("Creating course with code: {}", request.getCode());
        return courseRepository.findByCode(request.getCode())
                .flatMap(existing -> Mono.<Course>error(new DuplicateResourceException("Course with code " + request.getCode() + " already exists")))
                .switchIfEmpty(Mono.defer(() -> courseRepository.save(courseMapper.toEntity(request))))
                .map(courseMapper::toResponse);
    }

    @Override
    public Flux<CourseResponse> getAllCourses() {
        log.info("Fetching all courses");
        return courseRepository.findAll()
                .map(courseMapper::toResponse);
    }

    @Override
    public Mono<CourseResponse> getCourseById(Integer id) {
        log.info("Fetching course by id: {}", id);
        return courseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Course with id " + id + " not found")))
                .map(courseMapper::toResponse);
    }

    @Override
    @Transactional
    public Mono<CourseResponse> updateCourse(Integer id, CourseRequest request) {
        log.info("Updating course with id: {}", id);
        return courseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Course with id " + id + " not found")))
                .flatMap(existingCourse ->
                        courseRepository.findByCode(request.getCode())
                                .filter(found -> !found.getId().equals(id))
                                .flatMap(duplicate -> Mono.<Course>error(new DuplicateResourceException("Course code " + request.getCode() + " is already taken")))
                                .then(Mono.defer(() -> {
                                    courseMapper.updateEntity(existingCourse, request);
                                    return courseRepository.save(existingCourse);
                                }))
                )
                .map(courseMapper::toResponse);
    }

    @Override
    @Transactional
    public Mono<Void> deleteCourse(Integer id) {
        log.info("Deleting course with id: {}", id);
        return courseRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Course with id " + id + " not found")))
                .flatMap(courseRepository::delete);
    }
}
