package com.oexil.univote.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "voted_students")
public class VotedStudent {
    @Id
    @Column(name = "student_id")
    private String studentId;

    @Column(name = "voted_at")
    private LocalDateTime votedAt;
}