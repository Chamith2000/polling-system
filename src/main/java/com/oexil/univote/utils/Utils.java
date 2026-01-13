package com.oexil.univote.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Random;

@Service
public class Utils {

    public static String generateSixDigitsCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);
        return String.format("%06d", randomNumber);
    }

    public static String getBaseUrl() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr == null) return "http://localhost:8000"; // Default value if no request context
        HttpServletRequest request = attr.getRequest();

        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null) // Remove extra paths
                .build()
                .toUriString();
    }

    public static Integer calculatePercentage(Integer studentCount, Integer capacity) {
        try {
            if (studentCount == null) {
                return 0;
            }
            if (capacity == null || capacity <= 0) {
                return 0;
            }
            return ( capacity * 100) / studentCount;
        } catch (Exception e) {
            return 0;
        }
    }
}
