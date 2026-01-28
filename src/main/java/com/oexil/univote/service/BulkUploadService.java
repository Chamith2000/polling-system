package com.oexil.univote.service;

import com.oexil.univote.dto.BulkUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface BulkUploadService {
    BulkUploadResult uploadStudentsFromCSV(MultipartFile file);
    BulkUploadResult uploadStudentsFromExcel(MultipartFile file);
}