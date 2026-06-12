package com.school.studentservice.service;

import com.school.studentservice.dto.StudentRequest;
import com.school.studentservice.dto.StudentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {
    Mono<StudentResponse> createStudent(StudentRequest request);
    Flux<StudentResponse> getAllStudents();
    Mono<StudentResponse> getStudentById(Integer id);
    Mono<StudentResponse> updateStudent(Integer id, StudentRequest request);
    Mono<Void> deleteStudent(Integer id);
}
