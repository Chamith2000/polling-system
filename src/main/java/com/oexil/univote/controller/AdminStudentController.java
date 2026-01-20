package com.oexil.univote.controller;

import com.oexil.univote.model.Student;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("pageTitle", "Manage Students");
        return "admin/students/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("faculties", facultyRepository.findAll());
        model.addAttribute("pageTitle", "Create Student");
        model.addAttribute("isEdit", false);
        return "admin/students/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            studentRepository.save(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
            return "redirect:/admin/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating student: " + e.getMessage());
            return "redirect:/admin/students/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return studentRepository.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    model.addAttribute("faculties", facultyRepository.findAll());
                    model.addAttribute("pageTitle", "Edit Student");
                    model.addAttribute("isEdit", true);
                    return "admin/students/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Student not found!");
                    return "redirect:/admin/students";
                });
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable String id, @ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            student.setStudentId(id);
            studentRepository.save(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
            return "redirect:/admin/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating student: " + e.getMessage());
            return "redirect:/admin/students/edit/" + id;
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            studentRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }
}