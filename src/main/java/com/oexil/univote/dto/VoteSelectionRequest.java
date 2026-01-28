package com.oexil.univote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class VoteSelectionRequest {
    @NotNull
    private String studentId;

    @NotNull
    private Map<Long, Long> votes; // positionId -> candidateId
}