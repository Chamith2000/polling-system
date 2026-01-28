package com.oexil.univote.repository;

import com.oexil.univote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    // Get vote counts grouped by candidate
    @Query("SELECT v.candidate.id, COUNT(v) FROM Vote v GROUP BY v.candidate.id")
    List<Object[]> countVotesByCandidate();

    // Get total votes cast
    long count();
}