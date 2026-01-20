package com.oexil.univote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResponse {
    private boolean success;
    private String message;
    private VotingSessionDTO votingSession;
}