package com.oexil.univote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
    private Long candidateId;
    private String studentId;
    private String fullName;
    private String manifesto;
    private String photoUrl;
    private String facultyName;
}