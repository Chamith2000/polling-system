package com.oexil.univote.dto;

import lombok.Data;

import java.util.List;

@Data
public class PositionDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean isCommon;
    private Integer orderPriority;
    private List<CandidateDTO> candidates;
}