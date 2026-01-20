package com.oexil.univote.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "voted_students")
@Data
public class VotedStudent {
    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;

    @Column(name = "voted_at")
    private LocalDateTime votedAt = LocalDateTime.now();

    @Column(name = "ip_address", length = 50)
    private String ipAddress;
}