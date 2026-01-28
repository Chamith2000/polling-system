package com.oexil.univote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VotingActivationRequest {
    @NotBlank(message = "PIN is required")
    private String pin;
}