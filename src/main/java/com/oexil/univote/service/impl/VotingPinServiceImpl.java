package com.oexil.univote.service.impl;

import com.oexil.univote.model.VotingPin;
import com.oexil.univote.repository.VotingPinRepository;
import com.oexil.univote.service.VotingPinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VotingPinServiceImpl implements VotingPinService {

    private final VotingPinRepository votingPinRepository;

    @Override
    public Page<VotingPin> getAllPins(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").ascending());
        return votingPinRepository.findAll(pageable);
    }

    @Override
    public Optional<VotingPin> getPinById(Long id) {
        return votingPinRepository.findById(id);
    }

    @Override
    @Transactional
    public VotingPin createPin(VotingPin pin) {
        return votingPinRepository.save(pin);
    }

    @Override
    @Transactional
    public VotingPin updatePin(Long id, VotingPin pin) {
        pin.setId(id);
        return votingPinRepository.save(pin);
    }

    @Override
    @Transactional
    public void deletePin(Long id) {
        votingPinRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void togglePinActive(Long id) {
        votingPinRepository.findById(id).ifPresent(pin -> {
            pin.setIsActive(!pin.getIsActive());
            votingPinRepository.save(pin);
        });
    }
}