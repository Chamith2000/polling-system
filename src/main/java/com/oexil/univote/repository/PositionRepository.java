package com.oexil.univote.repository;

import com.oexil.univote.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByIsCommonOrderByOrderPriority(Boolean isCommon);
    List<Position> findAllByOrderByOrderPriority();
    Page<Position> findByNameContainingIgnoreCase(String name, Pageable pageable);
}