package com.school.courseservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("courses")
public class Course {
    @Id
    private Integer id;
    private String code;
    private String title;
    private String description;
    private Integer credits;
}
