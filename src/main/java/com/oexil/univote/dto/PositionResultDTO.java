package com.oexil.univote.dto;

import lombok.Data;

import java.util.List;

@Data
public class PositionResultDTO {
    private Long positionId;
    private String positionName;
    private Long totalVotesCast;
    private List<CandidateResultDTO> candidates;
    private CandidateResultDTO winner; // The candidate with the most votes
    private boolean isTie; // To indicate if there's a tie
}