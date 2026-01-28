package com.oexil.univote.repository;

import com.oexil.univote.model.Candidate;
import com.oexil.univote.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByPosition(Position position);

    // For common positions (faculty_id is NULL)
    List<Candidate> findByPosition_IdAndFacultyIsNull(Long positionId);

    // For faculty-specific positions
    List<Candidate> findByPosition_IdAndFaculty_Id(Long positionId, Long facultyId);

    @Query("SELECT c FROM Candidate c WHERE c.position.id = :positionId " +
            "AND (c.position.isCommon = true OR c.faculty.id = :facultyId)")
    List<Candidate> findByPositionAndFaculty(@Param("positionId") Long positionId,
                                             @Param("facultyId") Long facultyId);

    Page<Candidate> findByStudent_FullNameContainingIgnoreCaseOrStudent_StudentIdContainingIgnoreCase(String fullName, String studentId, Pageable pageable);
}