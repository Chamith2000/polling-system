package com.oexil.univote.dto;

import java.util.ArrayList;
import java.util.List;

public class BulkUploadResult {
    private List<String> successList = new ArrayList<>();
    private List<String> errorList = new ArrayList<>();
    private List<String> skippedList = new ArrayList<>();

    public void addSuccess(String studentId) {
        successList.add(studentId);
    }

    public void addError(int line, String message) {
        errorList.add("Line " + line + ": " + message);
    }

    public void addSkipped(int line, String message) {
        skippedList.add("Line " + line + ": " + message);
    }

    public int getSuccessCount() {
        return successList.size();
    }

    public int getErrorCount() {
        return errorList.size();
    }

    public int getSkippedCount() {
        return skippedList.size();
    }

    public List<String> getSuccessList() {
        return successList;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public List<String> getSkippedList() {
        return skippedList;
    }

    public boolean hasErrors() {
        return !errorList.isEmpty();
    }
}