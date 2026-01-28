package com.oexil.univote.constants;

public class Constants {
    public static final String IMAGE_URL = "http://localhost:8080/";

    // File Paths
    public static final String UPLOAD_DIR = "uploads";

    // Templates
    public static final String STUDENT_CSV_HEADER = "studentId,fullName,facultyCode,email,year";
    public static final String STUDENT_CSV_EXAMPLE = "ENG001,John Doe,ENG,john@example.com,3";

    // Messages
    public static final String ERR_STUDENT_NOT_FOUND = "Student not found";
    public static final String ERR_ALREADY_VOTED = "Student has already voted";
}
