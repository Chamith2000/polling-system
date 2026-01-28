package com.oexil.univote.service;

import com.oexil.univote.model.VotingPin;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface VotingPinService {
    Page<VotingPin> getAllPins(int pageNo, int pageSize); // Changed
    Optional<VotingPin> getPinById(Long id);
    VotingPin createPin(VotingPin pin);
    VotingPin updatePin(Long id, VotingPin pin);
    void deletePin(Long id);
    void togglePinActive(Long id);
}