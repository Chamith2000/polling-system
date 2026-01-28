package com.oexil.univote.service;

import com.oexil.univote.dto.PositionResultDTO;

import java.util.List;
import java.util.Map;

public interface ResultService {
    List<PositionResultDTO> getAllResults();
    Map<String, Object> getDashboardStats();
    PositionResultDTO getResultByPosition(Long positionId);
    void launchResult(Long positionId); // New method to broadcast via WebSocket
    void resetDisplay(); // New method to clear the public display
}