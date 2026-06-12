package com.school.studentservice.mapper;

import com.school.studentservice.dto.StudentRequest;
import com.school.studentservice.dto.StudentResponse;
import com.school.studentservice.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequest request) {
        if (request == null) {
            return null;
        }
        return Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .build();
    }

    public StudentResponse toResponse(Student entity) {
        if (entity == null) {
            return null;
        }
        return StudentResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .dateOfBirth(entity.getDateOfBirth())
                .build();
    }

    public void updateEntity(Student entity, StudentRequest request) {
        if (request == null || entity == null) {
            return;
        }
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEmail(request.getEmail());
        entity.setDateOfBirth(request.getDateOfBirth());
    }
}
