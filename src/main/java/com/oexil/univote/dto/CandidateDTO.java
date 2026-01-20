package com.oexil.univote.dto;

import lombok.Data;

@Data
public class CandidateDTO {
    private Long id;
    private String studentId;
    private String studentName;
    private String description;
    private String imageUrl;
    private String facultyName;
}