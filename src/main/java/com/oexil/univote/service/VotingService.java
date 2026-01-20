package com.oexil.univote.service;

import com.oexil.univote.dto.*;
import com.oexil.univote.model.*;
import com.oexil.univote.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final StudentRepository studentRepo;
    private final VotedStudentRepository votedStudentRepo;
    private final PositionRepository positionRepo;
    private final CandidateRepository candidateRepo;
    private final VoteRepository voteRepo;
    private final VotingPinRepository votingPinRepo;

    public boolean validatePin(String pin) {
        Optional<VotingPin> votingPin = votingPinRepo.findByPinValueAndIsActiveTrue(pin);
        if (votingPin.isEmpty()) {
            return false;
        }

        VotingPin vp = votingPin.get();
        LocalDateTime now = LocalDateTime.now();

        return vp.getActiveFrom().isBefore(now) && vp.getActiveUntil().isAfter(now);
    }

    public ValidationResponse validateStudent(String studentId) {
        // Check if student exists
        Optional<Student> studentOpt = studentRepo.findByStudentId(studentId);
        if (studentOpt.isEmpty()) {
            return new ValidationResponse(false, "Student ID not found", null);
        }

        // Check if already voted
        if (votedStudentRepo.existsByStudentId(studentId)) {
            return new ValidationResponse(false, "Student has already voted", null);
        }

        // Create voting session
        Student student = studentOpt.get();
        VotingSessionDTO session = createVotingSession(student);

        return new ValidationResponse(true, "Validation successful", session);
    }

    private VotingSessionDTO createVotingSession(Student student) {
        VotingSessionDTO session = new VotingSessionDTO();
        session.setStudentId(student.getStudentId());
        session.setStudentName(student.getFullName());
        session.setFacultyId(student.getFaculty().getId());
        session.setFacultyName(student.getFaculty().getName());
        session.setFacultyColor(student.getFaculty().getColorCode());
        session.setSelectedCandidates(new HashMap<>());
        session.setCurrentStep(0);

        // Get all positions for this student
        List<Position> allPositions = positionRepo.findAllByOrderByOrderPriority();
        List<PositionDTO> positionDTOs = new ArrayList<>();

        for (Position position : allPositions) {
            PositionDTO dto = new PositionDTO();
            dto.setId(position.getId());
            dto.setName(position.getName());
            dto.setDescription(position.getDescription());
            dto.setIsCommon(position.getIsCommon());
            dto.setOrderPriority(position.getOrderPriority());

            // Get candidates based on position type
            List<Candidate> candidates;

            if (position.getIsCommon()) {
                // For common positions, get all candidates (faculty_id is NULL)
                candidates = candidateRepo.findByPosition_IdAndFacultyIsNull(position.getId());
            } else {
                // For faculty-specific positions, get candidates for this faculty
                candidates = candidateRepo.findByPosition_IdAndFaculty_Id(
                        position.getId(),
                        student.getFaculty().getId()
                );
            }

            // Only add position if it has candidates
            if (!candidates.isEmpty()) {
                List<CandidateDTO> candidateDTOs = candidates.stream()
                        .map(this::toCandidateDTO)
                        .collect(Collectors.toList());

                dto.setCandidates(candidateDTOs);
                positionDTOs.add(dto);
            }
        }

        session.setPositions(positionDTOs);
        session.setTotalSteps(positionDTOs.size());

        return session;
    }

    private CandidateDTO toCandidateDTO(Candidate candidate) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(candidate.getId());
        dto.setStudentId(candidate.getStudent().getStudentId());
        dto.setStudentName(candidate.getStudent().getFullName());
        dto.setDescription(candidate.getDescription());
        dto.setImageUrl(candidate.getImageUrl());
        if (candidate.getFaculty() != null) {
            dto.setFacultyName(candidate.getFaculty().getName());
        }
        return dto;
    }

    @Transactional
    public ValidationResponse submitVotes(VoteSubmissionDTO submission, String ipAddress) {
        // Validate student hasn't voted
        if (votedStudentRepo.existsByStudentId(submission.getStudentId())) {
            return new ValidationResponse(false, "Student has already voted", null);
        }

        Student student = studentRepo.findByStudentId(submission.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Save all votes
        for (Map.Entry<Long, Long> entry : submission.getVotes().entrySet()) {
            Vote vote = new Vote();
            vote.setStudent(student);

            Position position = positionRepo.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            vote.setPosition(position);

            Candidate candidate = candidateRepo.findById(entry.getValue())
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));
            vote.setCandidate(candidate);

            voteRepo.save(vote);
        }

        // Mark student as voted
        VotedStudent votedStudent = new VotedStudent();
        votedStudent.setStudentId(submission.getStudentId());
        votedStudent.setIpAddress(ipAddress);
        votedStudentRepo.save(votedStudent);

        return new ValidationResponse(true, "Votes submitted successfully", null);
    }
}