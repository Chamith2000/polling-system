package com.oexil.univote.repository;

import com.oexil.univote.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Optional<Student> findByStudentId(String studentId);
    Page<Student> findByStudentIdContainingIgnoreCaseOrFullNameContainingIgnoreCase(String studentId, String fullName, Pageable pageable);
    // New: Search students within a specific faculty
    @Query("SELECT s FROM Student s WHERE s.faculty.code = :facultyCode " +
            "AND (LOWER(s.studentId) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(s.fullName) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Student> searchByFacultyAndQuery(@Param("facultyCode") String facultyCode, @Param("query") String query);
}