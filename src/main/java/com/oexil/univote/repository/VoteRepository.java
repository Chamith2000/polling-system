package com.oexil.univote.repository;

import com.oexil.univote.model.Vote;
import com.oexil.univote.model.Student;
import com.oexil.univote.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}