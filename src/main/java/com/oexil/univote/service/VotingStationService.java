package com.oexil.univote.service;

import com.oexil.univote.dto.VotingSessionDTO;

public interface VotingStationService {
    void activateBallot(String stationId, VotingSessionDTO session);
    void resetBallot(String stationId);
    VotingSessionDTO getActiveSession(String stationId);
    boolean hasActiveSession(String stationId);
}