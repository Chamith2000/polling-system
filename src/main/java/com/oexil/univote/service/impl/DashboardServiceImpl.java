package com.oexil.univote.service.impl;

import com.oexil.univote.model.Faculty;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FacultyRepository facultyRepository;

    @Override
    public List<Faculty> getFacultiesForDashboard() {
        List<Faculty> faculties = facultyRepository.findAll();

        log.info("=================================================");
        log.info("=== Loading Voting Dashboard ===");
        log.info("Total faculties found in database: {}", faculties.size());
        log.info("-------------------------------------------------");

        for (Faculty faculty : faculties) {
            log.info("Faculty ID: {} | Name: {} | Code: {} | Color: {}",
                    faculty.getId(),
                    faculty.getName(),
                    faculty.getCode(),
                    faculty.getColorCode()
            );
        }

        log.info("=================================================");

        return faculties;
    }
}