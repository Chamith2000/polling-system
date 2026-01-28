package com.oexil.univote.service;

import com.oexil.univote.dto.ValidationResponse;
import com.oexil.univote.dto.VoteSubmissionDTO;

import java.util.List;
import java.util.Map;

public interface VotingService {
    boolean validatePin(String pin);
    ValidationResponse validateStudent(String studentId);
    ValidationResponse submitVotes(VoteSubmissionDTO submission, String ipAddress);
    List<Map<String, Object>> searchStudentsForOperator(String stationId, String query);
    List<Map<String, Object>> getRecentVoters(String stationId);
    long getVoteCountByStation(String stationId);
}