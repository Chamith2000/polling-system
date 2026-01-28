package com.oexil.univote.service.impl;

import com.oexil.univote.dto.VotingSessionDTO;
import com.oexil.univote.service.VotingStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class VotingStationServiceImpl implements VotingStationService {

    private final SimpMessagingTemplate messagingTemplate;

    // Store active voting sessions by station ID (faculty code)
    private final Map<String, VotingSessionDTO> activeStations = new ConcurrentHashMap<>();

    @Override
    public void activateBallot(String stationId, VotingSessionDTO session) {
        activeStations.put(stationId, session);

        // Send WebSocket message to the tablet for this station
        messagingTemplate.convertAndSend(
                "/topic/station/" + stationId,
                Map.of(
                        "action", "ACTIVATE_BALLOT",
                        "session", session
                )
        );
    }

    @Override
    public void resetBallot(String stationId) {
        activeStations.remove(stationId);

        // Send WebSocket message to reset the tablet
        messagingTemplate.convertAndSend(
                "/topic/station/" + stationId,
                Map.of("action", "RESET_BALLOT")
        );
    }

    @Override
    public VotingSessionDTO getActiveSession(String stationId) {
        return activeStations.get(stationId);
    }

    @Override
    public boolean hasActiveSession(String stationId) {
        return activeStations.containsKey(stationId);
    }
}