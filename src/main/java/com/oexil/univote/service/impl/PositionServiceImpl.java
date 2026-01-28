package com.oexil.univote.service.impl;

import com.oexil.univote.model.Position;
import com.oexil.univote.repository.PositionRepository;
import com.oexil.univote.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    public Page<Position> getAllPositionsOrdered(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("orderPriority").ascending());
        return positionRepository.findAll(pageable);
    }

    @Override
    public List<Position> getAllPositionsList() {
        return positionRepository.findAllByOrderByOrderPriority();
    }

    @Override
    public Optional<Position> getPositionById(Long id) {
        return positionRepository.findById(id);
    }

    @Override
    @Transactional
    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    @Override
    @Transactional
    public Position updatePosition(Long id, Position position) {
        position.setId(id);
        return positionRepository.save(position);
    }

    @Override
    @Transactional
    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }

    @Override
    public Page<Position> getAllPositionsOrdered(int pageNo, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("orderPriority").ascending());

        if (keyword != null && !keyword.isEmpty()) {
            return positionRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }

        return positionRepository.findAll(pageable);
    }
}