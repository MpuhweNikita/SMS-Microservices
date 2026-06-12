package com.school.studentservice.service.impl;

import com.school.studentservice.dto.StudentRequest;
import com.school.studentservice.dto.StudentResponse;
import com.school.studentservice.entity.Student;
import com.school.studentservice.exception.DuplicateResourceException;
import com.school.studentservice.exception.ResourceNotFoundException;
import com.school.studentservice.mapper.StudentMapper;
import com.school.studentservice.repository.StudentRepository;
import com.school.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public Mono<StudentResponse> createStudent(StudentRequest request) {
        log.info("Creating student with email: {}", request.getEmail());
        return studentRepository.findByEmail(request.getEmail())
                .flatMap(existing -> Mono.<Student>error(new DuplicateResourceException("Student with email " + request.getEmail() + " already exists")))
                .switchIfEmpty(Mono.defer(() -> studentRepository.save(studentMapper.toEntity(request))))
                .map(studentMapper::toResponse);
    }

    @Override
    public Flux<StudentResponse> getAllStudents() {
        log.info("Fetching all students");
        return studentRepository.findAll()
                .map(studentMapper::toResponse);
    }

    @Override
    public Mono<StudentResponse> getStudentById(Integer id) {
        log.info("Fetching student by id: {}", id);
        return studentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student with id " + id + " not found")))
                .map(studentMapper::toResponse);
    }

    @Override
    @Transactional
    public Mono<StudentResponse> updateStudent(Integer id, StudentRequest request) {
        log.info("Updating student with id: {}", id);
        return studentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student with id " + id + " not found")))
                .flatMap(existingStudent -> 
                    studentRepository.findByEmail(request.getEmail())
                        .filter(found -> !found.getId().equals(id))
                        .flatMap(duplicate -> Mono.<Student>error(new DuplicateResourceException("Email " + request.getEmail() + " is already taken by another student")))
                        .then(Mono.defer(() -> {
                            studentMapper.updateEntity(existingStudent, request);
                            return studentRepository.save(existingStudent);
                        }))
                )
                .map(studentMapper::toResponse);
    }

    @Override
    @Transactional
    public Mono<Void> deleteStudent(Integer id) {
        log.info("Deleting student with id: {}", id);
        return studentRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Student with id " + id + " not found")))
                .flatMap(studentRepository::delete);
    }
}
