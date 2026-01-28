package com.oexil.univote.repository;

import com.oexil.univote.model.VotingPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingPinRepository extends JpaRepository<VotingPin, Long> {
    Optional<VotingPin> findByPinValueAndIsActiveTrue(String pinValue);
}