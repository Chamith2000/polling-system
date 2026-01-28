package com.oexil.univote.service;

import com.oexil.univote.model.Faculty;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    Page<Faculty> getAllFaculties(int pageNo, int pageSize); // Changed for pagination
    List<Faculty> getAllFacultiesList(); // Keep list for dropdowns
    Optional<Faculty> getFacultyById(Long id);
    Faculty createFaculty(Faculty faculty);
    Faculty updateFaculty(Long id, Faculty faculty);
    void deleteFaculty(Long id);
    Page<Faculty> getAllFaculties(int pageNo, int pageSize, String keyword);
}