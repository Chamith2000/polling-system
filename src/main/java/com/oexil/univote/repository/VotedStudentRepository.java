package com.oexil.univote.repository;

import com.oexil.univote.model.VotedStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotedStudentRepository extends JpaRepository<VotedStudent, String> {
    boolean existsByStudentId(String studentId);
}