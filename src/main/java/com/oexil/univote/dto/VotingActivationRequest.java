package com.oexil.univote.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class VotingActivationRequest {
    @NotBlank(message = "PIN is required")
    private String pin;
}