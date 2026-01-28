package com.oexil.univote.service.impl;

import com.oexil.univote.dto.BulkUploadResult;
import com.oexil.univote.model.Faculty;
import com.oexil.univote.model.Student;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.repository.StudentRepository;
import com.oexil.univote.service.BulkUploadService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BulkUploadServiceImpl implements BulkUploadService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Override
    @Transactional
    public BulkUploadResult uploadStudentsFromCSV(MultipartFile file) {
        BulkUploadResult result = new BulkUploadResult();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    String[] fields = line.split(",");

                    if (fields.length < 3) {
                        result.addError(lineNumber, "Invalid format - need at least: studentId, fullName, facultyCode");
                        continue;
                    }

                    String studentId = fields[0].trim();
                    String fullName = fields[1].trim();
                    String facultyCode = fields[2].trim();
                    String email = fields.length > 3 ? fields[3].trim() : null;
                    Integer year = fields.length > 4 && !fields[4].trim().isEmpty() ?
                            Integer.parseInt(fields[4].trim()) : null;

                    // Validate required fields
                    if (studentId.isEmpty() || fullName.isEmpty() || facultyCode.isEmpty()) {
                        result.addError(lineNumber, "Missing required fields");
                        continue;
                    }

                    // Check if student already exists
                    if (studentRepository.existsById(studentId)) {
                        result.addSkipped(lineNumber, studentId + " already exists");
                        continue;
                    }

                    // Find faculty
                    Optional<Faculty> facultyOpt = facultyRepository.findByCode(facultyCode.toUpperCase());
                    if (facultyOpt.isEmpty()) {
                        result.addError(lineNumber, "Faculty code '" + facultyCode + "' not found");
                        continue;
                    }

                    // Create student
                    Student student = new Student();
                    student.setStudentId(studentId);
                    student.setFullName(fullName);
                    student.setFaculty(facultyOpt.get());
                    student.setEmail(email);
                    student.setYear(year);

                    studentRepository.save(student);
                    result.addSuccess(studentId);

                } catch (Exception e) {
                    result.addError(lineNumber, "Error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            result.addError(0, "File processing error: " + e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional
    public BulkUploadResult uploadStudentsFromExcel(MultipartFile file) {
        BulkUploadResult result = new BulkUploadResult();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowNumber = 0;

            for (Row row : sheet) {
                rowNumber++;

                // Skip header row
                if (rowNumber == 1) {
                    continue;
                }

                // Skip empty rows
                if (row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }

                try {
                    String studentId = getCellValue(row.getCell(0));
                    String fullName = getCellValue(row.getCell(1));
                    String facultyCode = getCellValue(row.getCell(2));
                    String email = row.getCell(3) != null ? getCellValue(row.getCell(3)) : null;
                    Integer year = row.getCell(4) != null ? parseIntegerCell(row.getCell(4)) : null;

                    // Validate required fields
                    if (studentId.isEmpty() || fullName.isEmpty() || facultyCode.isEmpty()) {
                        result.addError(rowNumber, "Missing required fields");
                        continue;
                    }

                    // Check if student already exists
                    if (studentRepository.existsById(studentId)) {
                        result.addSkipped(rowNumber, studentId + " already exists");
                        continue;
                    }

                    // Find faculty
                    Optional<Faculty> facultyOpt = facultyRepository.findByCode(facultyCode.toUpperCase());
                    if (facultyOpt.isEmpty()) {
                        result.addError(rowNumber, "Faculty code '" + facultyCode + "' not found");
                        continue;
                    }

                    // Create student
                    Student student = new Student();
                    student.setStudentId(studentId);
                    student.setFullName(fullName);
                    student.setFaculty(facultyOpt.get());
                    student.setEmail(email);
                    student.setYear(year);

                    studentRepository.save(student);
                    result.addSuccess(studentId);

                } catch (Exception e) {
                    result.addError(rowNumber, "Error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            result.addError(0, "File processing error: " + e.getMessage());
        }

        return result;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Integer parseIntegerCell(Cell cell) {
        if (cell == null) {
            return null;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                return value.isEmpty() ? null : Integer.parseInt(value);
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }
}