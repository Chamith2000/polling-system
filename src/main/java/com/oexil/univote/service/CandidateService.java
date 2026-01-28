package com.oexil.univote.service;

import com.oexil.univote.model.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CandidateService {
    Page<Candidate> getAllCandidates(int pageNo, int pageSize); // Changed
    Optional<Candidate> getCandidateById(Long id);
    Candidate createCandidate(Candidate candidate, String studentId, Long positionId, Long facultyId, MultipartFile image);
    Candidate updateCandidate(Long id, Candidate candidate, String studentId, Long positionId, Long facultyId, MultipartFile image);
    void deleteCandidate(Long id);
    Page<Candidate> getAllCandidates(int pageNo, int pageSize, String keyword);
}