package com.oexil.univote.service;

import com.oexil.univote.model.Faculty;

import java.util.List;

public interface DashboardService {
    List<Faculty> getFacultiesForDashboard();
}