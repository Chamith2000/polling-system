package com.oexil.univote.dto;

import lombok.Data;

@Data
public class CandidateResultDTO {
    private Long id;
    private String name;
    private String studentId;
    private String imageUrl;
    private Long voteCount;
    private Double percentage;
    private String facultyName; // To show if they represent a specific faculty
}