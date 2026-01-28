package com.oexil.univote.service.impl;

import com.oexil.univote.dto.CandidateResultDTO;
import com.oexil.univote.dto.PositionResultDTO;
import com.oexil.univote.dto.ResultAnnouncementDTO;
import com.oexil.univote.model.Candidate;
import com.oexil.univote.model.Position;
import com.oexil.univote.repository.CandidateRepository;
import com.oexil.univote.repository.PositionRepository;
import com.oexil.univote.repository.VoteRepository;
import com.oexil.univote.repository.VotedStudentRepository;
import com.oexil.univote.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final VoteRepository voteRepository;
    private final PositionRepository positionRepository;
    private final CandidateRepository candidateRepository;
    private final VotedStudentRepository votedStudentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public List<PositionResultDTO> getAllResults() {
        // 1. Fetch all vote counts in one query (CandidateID -> Count)
        List<Object[]> rawCounts = voteRepository.countVotesByCandidate();
        Map<Long, Long> voteCounts = new HashMap<>();
        for (Object[] row : rawCounts) {
            voteCounts.put((Long) row[0], (Long) row[1]);
        }

        // 2. Fetch all positions and candidates
        List<Position> positions = positionRepository.findAllByOrderByOrderPriority();
        List<Candidate> allCandidates = candidateRepository.findAll();

        // 3. Group candidates by position
        Map<Long, List<Candidate>> candidatesByPosition = allCandidates.stream()
                .collect(Collectors.groupingBy(c -> c.getPosition().getId()));

        List<PositionResultDTO> results = new ArrayList<>();

        for (Position position : positions) {
            PositionResultDTO positionResult = new PositionResultDTO();
            positionResult.setPositionId(position.getId());
            positionResult.setPositionName(position.getName());

            List<Candidate> candidates = candidatesByPosition.getOrDefault(position.getId(), Collections.emptyList());
            List<CandidateResultDTO> candidateResults = new ArrayList<>();
            long positionTotalVotes = 0;

            // Process each candidate for this position
            for (Candidate candidate : candidates) {
                long votes = voteCounts.getOrDefault(candidate.getId(), 0L);
                positionTotalVotes += votes;

                CandidateResultDTO dto = new CandidateResultDTO();
                dto.setId(candidate.getId());
                dto.setName(candidate.getStudent().getFullName());
                dto.setStudentId(candidate.getStudent().getStudentId());
                dto.setImageUrl(candidate.getImageUrl());
                dto.setVoteCount(votes);
                dto.setFacultyName(candidate.getFaculty() != null ? candidate.getFaculty().getName() : "Common");

                candidateResults.add(dto);
            }

            // Calculate percentages
            for (CandidateResultDTO dto : candidateResults) {
                if (positionTotalVotes > 0) {
                    double percentage = (double) dto.getVoteCount() / positionTotalVotes * 100;
                    dto.setPercentage(Math.round(percentage * 10.0) / 10.0); // Round to 1 decimal
                } else {
                    dto.setPercentage(0.0);
                }
            }

            // Sort by vote count descending
            candidateResults.sort(Comparator.comparingLong(CandidateResultDTO::getVoteCount).reversed());

            // Determine winner
            if (!candidateResults.isEmpty() && positionTotalVotes > 0) {
                CandidateResultDTO topCandidate = candidateResults.get(0);
                // Check for tie
                boolean isTie = candidateResults.size() > 1 &&
                        candidateResults.get(1).getVoteCount().equals(topCandidate.getVoteCount());

                positionResult.setTie(isTie);
                if (!isTie) {
                    positionResult.setWinner(topCandidate);
                }
            }

            positionResult.setTotalVotesCast(positionTotalVotes);
            positionResult.setCandidates(candidateResults);
            results.add(positionResult);
        }

        return results;
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVotes", votedStudentRepository.count());
        // You can add more global stats here if needed
        return stats;
    }

    @Override
    public PositionResultDTO getResultByPosition(Long positionId) {
        // Fetch all results and filter for the specific position
        return getAllResults().stream()
                .filter(r -> r.getPositionId().equals(positionId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void launchResult(Long positionId) {
        PositionResultDTO result = getResultByPosition(positionId);
        if (result != null) {
            // Send to WebSocket topic /topic/results/display
            messagingTemplate.convertAndSend("/topic/results/display",
                    new ResultAnnouncementDTO("SHOW_RESULT", result));
        }
    }

    @Override
    public void resetDisplay() {
        messagingTemplate.convertAndSend("/topic/results/display",
                new ResultAnnouncementDTO("RESET", null));
    }
}