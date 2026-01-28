package com.oexil.univote.service;

import com.oexil.univote.dto.BulkUploadResult;
import com.oexil.univote.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface StudentService {
    Page<Student> getAllStudents(int pageNo, int pageSize); // Changed to return Page
    Optional<Student> getStudentById(String id);
    Student createStudent(Student student);
    Student updateStudent(String id, Student student);
    void deleteStudent(String id);
    BulkUploadResult bulkUploadFromFile(MultipartFile file);
    String getCSVTemplate();
    Page<Student> getAllStudents(int pageNo, int pageSize, String keyword);
    byte[] getExcelTemplate();
}