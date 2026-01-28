package com.oexil.univote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentValidationRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;
}