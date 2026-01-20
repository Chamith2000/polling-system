package com.oexil.univote.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class VotingSessionDTO {
    private String studentId;
    private String studentName;
    private Long facultyId;
    private String facultyName;
    private String facultyColor;
    private List<PositionDTO> positions;
    private Map<Long, Long> selectedCandidates; // positionId -> candidateId
    private Integer currentStep;
    private Integer totalSteps;
}