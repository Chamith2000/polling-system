package com.oexil.univote.service;

import com.oexil.univote.model.Position;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface PositionService {
    Page<Position> getAllPositionsOrdered(int pageNo, int pageSize); // Changed
    List<Position> getAllPositionsList(); // Keep list for dropdowns
    Optional<Position> getPositionById(Long id);
    Position createPosition(Position position);
    Position updatePosition(Long id, Position position);
    void deletePosition(Long id);
    Page<Position> getAllPositionsOrdered(int pageNo, int pageSize, String keyword);
}