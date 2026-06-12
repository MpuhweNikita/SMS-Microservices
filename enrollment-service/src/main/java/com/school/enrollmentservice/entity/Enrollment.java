package com.school.enrollmentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("enrollments")
public class Enrollment {
    @Id
    private Integer id;

    @Column("student_id")
    private Integer studentId;

    @Column("course_id")
    private Integer courseId;

    @Column("enrollment_date")
    private LocalDateTime enrollmentDate;

    private String status;
}
