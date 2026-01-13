package com.oexil.univote.controller;

import com.oexil.univote.model.Candidate;
import com.oexil.univote.model.Position;
import com.oexil.univote.model.Student;
import com.oexil.univote.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/voting")
public class VotingController {

    @Autowired
    private VotingService votingService;

    @GetMapping("/success")
    public String showSuccessPage() {
        return "voting/success";
    }

    // Step 1: Show PIN Entry Page
    @GetMapping("/pin")
    public String showPinPage() {
        return "voting/enter-pin"; // You need to create this HTML
    }

    @PostMapping("/check-pin")
    public String checkPin(@RequestParam String pin, Model model) {
        if (votingService.validatePin(pin)) {
            return "redirect:/voting/student-id";
        }
        model.addAttribute("error", "Invalid PIN");
        return "voting/enter-pin";
    }

    // Step 2: Enter Student ID
    @GetMapping("/student-id")
    public String showStudentIdPage() {
        return "voting/enter-student-id"; // You need to create this HTML
    }

    @PostMapping("/validate-student")
    public String validateStudent(@RequestParam String studentId, Model model) {
        try {
            Student student = votingService.validateStudentForVoting(studentId);
            model.addAttribute("student", student);

            // Load Ballot Data
            Map<Position, List<Candidate>> ballot = votingService.getBallotPaper(student);
            model.addAttribute("ballot", ballot);

            return "voting/ballot-paper"; // Main voting UI
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "voting/enter-student-id";
        }
    }

    // Step 3: Submit Vote (AJAX is better, but this is form submission)
    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<?> submitVotes(@RequestParam String studentId, @RequestBody List<Long> candidateIds) {
        try {
            votingService.submitVote(studentId, candidateIds);
            return ResponseEntity.ok("Vote Submitted Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}