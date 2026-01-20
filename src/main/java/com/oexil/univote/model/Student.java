package com.oexil.univote.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
public class Student {
    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(length = 100)
    private String email;

    private Integer year;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}