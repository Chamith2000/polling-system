package com.oexil.univote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentVotingSession {
    private String studentId;
    private String studentName;
    private Long facultyId;
    private String facultyName;
    private String facultyColorCode;
}