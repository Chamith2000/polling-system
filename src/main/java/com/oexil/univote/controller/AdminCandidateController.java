package com.oexil.univote.controller;

import com.oexil.univote.model.Candidate;
import com.oexil.univote.repository.CandidateRepository;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.repository.PositionRepository;
import com.oexil.univote.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/candidates")
@RequiredArgsConstructor
public class AdminCandidateController {

    private final CandidateRepository candidateRepository;
    private final StudentRepository studentRepository;
    private final PositionRepository positionRepository;
    private final FacultyRepository facultyRepository;

    @GetMapping
    public String listCandidates(Model model) {
        model.addAttribute("candidates", candidateRepository.findAll());
        model.addAttribute("pageTitle", "Manage Candidates");
        return "admin/candidates/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("candidate", new Candidate());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("positions", positionRepository.findAllByOrderByOrderPriority());
        model.addAttribute("faculties", facultyRepository.findAll());
        model.addAttribute("pageTitle", "Create Candidate");
        model.addAttribute("isEdit", false);
        return "admin/candidates/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate,
                         @RequestParam String studentId,
                         @RequestParam Long positionId,
                         @RequestParam(required = false) Long facultyId,
                         RedirectAttributes redirectAttributes) {
        try {
            studentRepository.findById(studentId).ifPresent(candidate::setStudent);
            positionRepository.findById(positionId).ifPresent(candidate::setPosition);
            if (facultyId != null) {
                facultyRepository.findById(facultyId).ifPresent(candidate::setFaculty);
            }

            candidateRepository.save(candidate);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate created successfully!");
            return "redirect:/admin/candidates";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating candidate: " + e.getMessage());
            return "redirect:/admin/candidates/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return candidateRepository.findById(id)
                .map(candidate -> {
                    model.addAttribute("candidate", candidate);
                    model.addAttribute("students", studentRepository.findAll());
                    model.addAttribute("positions", positionRepository.findAllByOrderByOrderPriority());
                    model.addAttribute("faculties", facultyRepository.findAll());
                    model.addAttribute("pageTitle", "Edit Candidate");
                    model.addAttribute("isEdit", true);
                    return "admin/candidates/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Candidate not found!");
                    return "redirect:/admin/candidates";
                });
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Candidate candidate,
                         @RequestParam String studentId,
                         @RequestParam Long positionId,
                         @RequestParam(required = false) Long facultyId,
                         RedirectAttributes redirectAttributes) {
        try {
            candidate.setId(id);
            studentRepository.findById(studentId).ifPresent(candidate::setStudent);
            positionRepository.findById(positionId).ifPresent(candidate::setPosition);
            if (facultyId != null) {
                facultyRepository.findById(facultyId).ifPresent(candidate::setFaculty);
            }

            candidateRepository.save(candidate);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate updated successfully!");
            return "redirect:/admin/candidates";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating candidate: " + e.getMessage());
            return "redirect:/admin/candidates/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            candidateRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting candidate: " + e.getMessage());
        }
        return "redirect:/admin/candidates";
    }
}