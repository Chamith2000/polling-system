package com.oexil.univote.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class StudentValidationRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;
}