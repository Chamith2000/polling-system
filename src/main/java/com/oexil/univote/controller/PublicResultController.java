package com.oexil.univote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public/results")
public class PublicResultController {

    @GetMapping("/launch")
    public String publicResultPage() {
        // This opens a dedicated clean page for display/projector
        return "voting/result-launch";
    }
}