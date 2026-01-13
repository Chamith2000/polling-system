package com.oexil.univote.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "student_id", nullable = false)
    private String studentId; // e.g., AA1234

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    private String email;
}