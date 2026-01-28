package com.oexil.univote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultAnnouncementDTO {
    private String action; // e.g., "SHOW_RESULT" or "RESET"
    private PositionResultDTO result;
}