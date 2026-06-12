package com.school.courseservice.controller;

import com.school.courseservice.dto.CourseRequest;
import com.school.courseservice.dto.CourseResponse;
import com.school.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public Mono<ResponseEntity<CourseResponse>> createCourse(@Valid @RequestBody CourseRequest request) {
        return courseService.createCourse(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @GetMapping
    public Flux<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CourseResponse>> getCourseById(@PathVariable Integer id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CourseResponse>> updateCourse(@PathVariable Integer id, @Valid @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCourse(@PathVariable Integer id) {
        return courseService.deleteCourse(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build()));
    }
}
