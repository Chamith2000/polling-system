package com.oexil.univote.repository;

import com.oexil.univote.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByPositionId(Long positionId);

    @Query("SELECT c FROM Candidate c WHERE c.position.isCommon = true OR c.student.faculty.id = :facultyId")
    List<Candidate> findCandidatesForStudent(Long facultyId);
}