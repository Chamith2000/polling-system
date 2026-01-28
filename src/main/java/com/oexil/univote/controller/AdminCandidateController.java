package com.oexil.univote.controller;

import com.oexil.univote.model.Candidate;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.repository.PositionRepository;
import com.oexil.univote.repository.StudentRepository;
import com.oexil.univote.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/candidates")
@RequiredArgsConstructor
public class AdminCandidateController {

    private final CandidateService candidateService;
    private final StudentRepository studentRepository;
    private final PositionRepository positionRepository;
    private final FacultyRepository facultyRepository;

    @GetMapping
    public String listCandidates(@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(required = false) String keyword,
                                 Model model) {
        int pageSize = 20;
        Page<Candidate> candidatePage = candidateService.getAllCandidates(page, pageSize, keyword);

        model.addAttribute("candidates", candidatePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", candidatePage.getTotalPages());
        model.addAttribute("totalItems", candidatePage.getTotalElements());
        model.addAttribute("keyword", keyword);
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

    // UPDATE: Accept MultipartFile image
    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate,
                         @RequestParam String studentId,
                         @RequestParam Long positionId,
                         @RequestParam(required = false) Long facultyId,
                         @RequestParam(required = false) MultipartFile image, // Image parameter
                         RedirectAttributes redirectAttributes) {
        try {
            candidateService.createCandidate(candidate, studentId, positionId, facultyId, image);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate created successfully!");
            return "redirect:/admin/candidates";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating candidate: " + e.getMessage());
            return "redirect:/admin/candidates/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return candidateService.getCandidateById(id)
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

    // UPDATE: Accept MultipartFile image
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Candidate candidate,
                         @RequestParam String studentId,
                         @RequestParam Long positionId,
                         @RequestParam(required = false) Long facultyId,
                         @RequestParam(required = false) MultipartFile image, // Image parameter
                         RedirectAttributes redirectAttributes) {
        try {
            candidateService.updateCandidate(id, candidate, studentId, positionId, facultyId, image);
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
            candidateService.deleteCandidate(id);
            redirectAttributes.addFlashAttribute("successMessage", "Candidate deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting candidate: " + e.getMessage());
        }
        return "redirect:/admin/candidates";
    }
}