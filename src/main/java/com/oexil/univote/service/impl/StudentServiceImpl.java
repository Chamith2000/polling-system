package com.oexil.univote.service.impl;

import com.oexil.univote.constants.Constants;
import com.oexil.univote.dto.BulkUploadResult;
import com.oexil.univote.model.Student;
import com.oexil.univote.repository.StudentRepository;
import com.oexil.univote.service.BulkUploadService;
import com.oexil.univote.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final BulkUploadService bulkUploadService;

    @Override
    public Page<Student> getAllStudents(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").ascending());
        return studentRepository.findAll(pageable);
    }

    @Override
    public Optional<Student> getStudentById(String id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student updateStudent(String id, Student student) {
        student.setStudentId(id);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudent(String id) {
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BulkUploadResult bulkUploadFromFile(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (filename != null && filename.endsWith(".csv")) {
            return bulkUploadService.uploadStudentsFromCSV(file);
        } else if (filename != null && (filename.endsWith(".xlsx") || filename.endsWith(".xls"))) {
            return bulkUploadService.uploadStudentsFromExcel(file);
        } else {
            throw new IllegalArgumentException("Invalid file format. Please upload CSV or Excel file.");
        }
    }

    @Override
    public String getCSVTemplate() {
        return Constants.STUDENT_CSV_HEADER + "\n" +
                Constants.STUDENT_CSV_EXAMPLE + "\n" +
                "IT001,Jane Smith,IT,jane@example.com,2\n" +
                "BM001,Bob Wilson,BM,bob@example.com,1";
    }

    @Override
    public Page<Student> getAllStudents(int pageNo, int pageSize, String keyword) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by("createdAt").ascending());

        if (keyword != null && !keyword.isEmpty()) {
            return studentRepository.findByStudentIdContainingIgnoreCaseOrFullNameContainingIgnoreCase(keyword, keyword, pageable);
        }
        return studentRepository.findAll(pageable);
    }

    @Override
    public byte[] getExcelTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"studentId", "fullName", "facultyCode", "email", "year"};

            // Header Style (Optional - Bold Headers)
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Example Data Row
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("ENG001");
            row.createCell(1).setCellValue("John Doe");
            row.createCell(2).setCellValue("ENG");
            row.createCell(3).setCellValue("john@example.com");
            row.createCell(4).setCellValue(3);

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel template", e);
        }
    }
}