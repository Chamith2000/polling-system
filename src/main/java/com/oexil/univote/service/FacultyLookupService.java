package com.oexil.univote.service;

import com.oexil.univote.model.Faculty;

import java.util.List;

public interface FacultyLookupService {
    String getStationName(String stationId);
    String getFacultyCode(Long facultyId);
    List<Faculty> getAllFaculties();
    String getStationColor(String stationId);
}