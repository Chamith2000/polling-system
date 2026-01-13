package com.oexil.univote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteSummary {
    private String positionName;
    private String candidateName;
    private String candidateStudentId;
}