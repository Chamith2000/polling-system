package com.oexil.univote.dto;

import lombok.Data;
import java.util.Map;

@Data
public class VoteSubmissionDTO {
    private String studentId;
    private Map<Long, Long> votes; // positionId -> candidateId
}