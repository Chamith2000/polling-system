package com.oexil.univote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionWithCandidates {
    private Long positionId;
    private String positionName;
    private String description;
    private Boolean isCommon;
    private Integer orderPriority;
    private List<CandidateDTO> candidates;
}