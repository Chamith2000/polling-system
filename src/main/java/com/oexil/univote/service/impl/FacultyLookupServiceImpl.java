package com.oexil.univote.service.impl;

import com.oexil.univote.model.Faculty;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.service.FacultyLookupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacultyLookupServiceImpl implements FacultyLookupService {

    private final FacultyRepository facultyRepository;

    @Override
    public String getStationName(String stationId) {
        return facultyRepository.findByCode(stationId)
                .map(faculty -> faculty.getName() + " Faculty")
                .orElse("Unknown Faculty");
    }

    @Override
    public String getStationColor(String stationId) {
        return facultyRepository.findByCode(stationId)
                .map(Faculty::getColorCode)
                .orElse("#1e3c72"); // Default Blue color ekak (facuty eka hoyaganna bari nam)
    }

    @Override
    public String getFacultyCode(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .map(Faculty::getCode)
                .orElse("UNKNOWN");
    }

    @Override
    public List<Faculty> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();

        log.info("=== Station Selection Page ===");
        log.info("Loading faculties for station selection");
        log.info("Total faculties: {}", faculties.size());
        faculties.forEach(f -> log.info("  - {} ({})", f.getName(), f.getCode()));
        log.info("==============================");

        return faculties;
    }
}