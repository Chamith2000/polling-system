package com.oexil.univote.service.impl;

import com.oexil.univote.model.Candidate;
import com.oexil.univote.repository.CandidateRepository;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.repository.PositionRepository;
import com.oexil.univote.repository.StudentRepository;
import com.oexil.univote.service.CandidateService;
import com.oexil.univote.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final StudentRepository studentRepository;
    private final PositionRepository positionRepository;
    private final FacultyRepository facultyRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public Page<Candidate> getAllCandidates(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").ascending());
        return candidateRepository.findAll(pageable);
    }

    @Override
    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    @Override
    @Transactional
    public Candidate createCandidate(Candidate candidate, String studentId, Long positionId, Long facultyId, MultipartFile image) {
        studentRepository.findById(studentId).ifPresent(candidate::setStudent);
        positionRepository.findById(positionId).ifPresent(candidate::setPosition);
        if (facultyId != null) {
            facultyRepository.findById(facultyId).ifPresent(candidate::setFaculty);
        }

        // Image upload logic
        if (image != null && !image.isEmpty()) {
            String[] uploadResult = imageUploadService.getResultsOfFileWrite(image);
            if (uploadResult != null) {
                // uploadResult[1] contains the relative URL (e.g., /files/123.jpg)
                candidate.setImageUrl(uploadResult[1]);
            }
        }

        return candidateRepository.save(candidate);
    }

    @Override
    @Transactional
    public Candidate updateCandidate(Long id, Candidate candidate, String studentId, Long positionId, Long facultyId, MultipartFile image) {
        // Fetch existing candidate to preserve image if new one is not uploaded
        Candidate existingCandidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        candidate.setId(id);
        studentRepository.findById(studentId).ifPresent(candidate::setStudent);
        positionRepository.findById(positionId).ifPresent(candidate::setPosition);
        if (facultyId != null) {
            facultyRepository.findById(facultyId).ifPresent(candidate::setFaculty);
        }

        // Image upload logic
        if (image != null && !image.isEmpty()) {
            String[] uploadResult = imageUploadService.getResultsOfFileWrite(image);
            if (uploadResult != null) {
                candidate.setImageUrl(uploadResult[1]);
            }
        } else {
            // If no new image uploaded, keep the old one
            candidate.setImageUrl(existingCandidate.getImageUrl());
        }

        return candidateRepository.save(candidate);
    }
    @Override
    @Transactional
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }

    @Override
    public Page<Candidate> getAllCandidates(int pageNo, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("id").ascending());

        if (keyword != null && !keyword.isEmpty()) {
            return candidateRepository.findByStudent_FullNameContainingIgnoreCaseOrStudent_StudentIdContainingIgnoreCase(keyword, keyword, pageable);
        }
        return candidateRepository.findAll(pageable);
    }
}