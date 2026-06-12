package com.school.courseservice.mapper;

import com.school.courseservice.dto.CourseRequest;
import com.school.courseservice.dto.CourseResponse;
import com.school.courseservice.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequest request) {
        if (request == null) {
            return null;
        }
        return Course.builder()
                .code(request.getCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .credits(request.getCredits())
                .build();
    }

    public CourseResponse toResponse(Course entity) {
        if (entity == null) {
            return null;
        }
        return CourseResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .credits(entity.getCredits())
                .build();
    }

    public void updateEntity(Course entity, CourseRequest request) {
        if (request == null || entity == null) {
            return;
        }
        entity.setCode(request.getCode());
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCredits(request.getCredits());
    }
}
