package com.oexil.univote.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "faculties")
@Data
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "color_code", nullable = false, length = 7)
    private String colorCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}