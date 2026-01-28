package com.oexil.univote.repository;

import com.oexil.univote.model.VotedStudent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotedStudentRepository extends JpaRepository<VotedStudent, String> {
    boolean existsByStudentId(String studentId);
    // New: Get recent voters for a specific faculty
    // We join with the Student table to check the faculty code
    @Query("SELECT v FROM VotedStudent v WHERE v.studentId IN " +
            "(SELECT s.studentId FROM Student s WHERE s.faculty.code = :facultyCode) " +
            "ORDER BY v.votedAt DESC")
    List<VotedStudent> findRecentVotesByFaculty(@Param("facultyCode") String facultyCode, Pageable pageable);

    @Query("SELECT COUNT(v) FROM VotedStudent v WHERE v.studentId IN " +
            "(SELECT s.studentId FROM Student s WHERE s.faculty.code = :facultyCode)")
    long countByFacultyCode(@Param("facultyCode") String facultyCode);
}