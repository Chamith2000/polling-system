//package com.oexil.univote.service.impl;
//
//import com.oexil.univote.model.*;
//import com.oexil.univote.repository.*;
//import com.oexil.univote.service.VotingServiceold;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class VotingServiceImpl implements VotingServiceold {
//
//    @Autowired
//    private VotingPinRepository pinRepository;
//    @Autowired
//    private StudentRepository studentRepository;
//    @Autowired
//    private VotedStudentRepository votedStudentRepository;
//    @Autowired
//    private PositionRepository positionRepository;
//    @Autowired
//    private CandidateRepository candidateRepository;
//    @Autowired
//    private VoteRepository voteRepository;
//
//    @Override
//    public boolean validatePin(String pin) {
//        return pinRepository.findByPinValueAndIsActiveTrue(pin).isPresent();
//    }
//
//    @Override
//    public Student validateStudentForVoting(String studentId) throws Exception {
//        Optional<Student> studentOpt = studentRepository.findById(studentId);
//        if (!studentOpt.isPresent()) {
//            throw new Exception("Student not found!");
//        }
//
//        if (votedStudentRepository.existsByStudentId(studentId)) {
//            throw new Exception("Student has already voted!");
//        }
//
//        return studentOpt.get();
//    }
//
//    @Override
//    public Map<Position, List<Candidate>> getBallotPaper(Student student) {
//        // Get all positions ordered by priority
//        List<Position> allPositions = positionRepository.findAllByOrderByOrderPriorityAsc();
//
//        // Get all candidates suitable for this student
//        // (Either common position candidates OR candidates from same faculty)
//        List<Candidate> allCandidates = candidateRepository.findAll();
//
//        Map<Position, List<Candidate>> ballot = new LinkedHashMap<>();
//
//        for (Position pos : allPositions) {
//            // Filter candidates for this position
//            List<Candidate> posCandidates = allCandidates.stream()
//                    .filter(c -> c.getPosition().getId().equals(pos.getId()))
//                    .filter(c -> {
//                        // Logic: If position is common, show all. If faculty specific, show only matching faculty.
//                        if (pos.isCommon()) return true;
//                        return c.getStudent().getFaculty().getId().equals(student.getFaculty().getId());
//                    })
//                    .collect(Collectors.toList());
//
//            if (!posCandidates.isEmpty()) {
//                ballot.put(pos, posCandidates);
//            }
//        }
//        return ballot;
//    }
//
//    @Override
//    @Transactional
//    public void submitVote(String studentId, List<Long> candidateIds) throws Exception {
//        if (votedStudentRepository.existsByStudentId(studentId)) {
//            throw new Exception("Double voting detected!");
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//
//        for (Long candidateId : candidateIds) {
//            Candidate candidate = candidateRepository.findById(candidateId)
//                    .orElseThrow(() -> new Exception("Invalid candidate ID"));
//
//            Vote vote = new Vote();
//            vote.setPosition(candidate.getPosition());
//            vote.setCandidate(candidate);
//            vote.setVotedAt(now);
//            voteRepository.save(vote);
//        }
//
//        // Mark student as voted
//        VotedStudent votedStudent = new VotedStudent(studentId, now);
//        votedStudentRepository.save(votedStudent);
//    }
//}