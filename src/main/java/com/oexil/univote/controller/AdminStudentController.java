package com.oexil.univote.controller;

import com.oexil.univote.model.Student;
import com.oexil.univote.repository.FacultyRepository;
import com.oexil.univote.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/students")
@RequiredArgsConstructor
public class AdminStudentController {

    private final StudentService studentService;
    private final FacultyRepository facultyRepository;

//    @GetMapping
//    public String listStudents(@RequestParam(defaultValue = "1") int page, Model model) {
//        int pageSize = 20;
//        Page<Student> studentPage = studentService.getAllStudents(page, pageSize);
//
//        model.addAttribute("students", studentPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", studentPage.getTotalPages());
//        model.addAttribute("totalItems", studentPage.getTotalElements());
//        model.addAttribute("pageTitle", "Manage Students");
//
//        return "admin/students/list";
//    }

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
            studentService.createStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
            return "redirect:/admin/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating student: " + e.getMessage());
            return "redirect:/admin/students/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return studentService.getStudentById(id)
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
            studentService.updateStudent(id, student);
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
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }

    @GetMapping("/bulk-upload")
    public String bulkUploadForm(Model model) {
        model.addAttribute("pageTitle", "Bulk Upload Students");
        return "admin/students/bulk-upload";
    }

    @PostMapping("/bulk-upload")
    public String bulkUpload(@RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload");
            return "redirect:/admin/students/bulk-upload";
        }

        try {
            var result = studentService.bulkUploadFromFile(file);

            redirectAttributes.addFlashAttribute("uploadResult", result);

            if (result.getSuccessCount() > 0) {
                redirectAttributes.addFlashAttribute("successMessage",
                        String.format("Successfully uploaded %d students!", result.getSuccessCount()));
            }

            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("warningMessage",
                        String.format("Upload completed with %d errors and %d skipped",
                                result.getErrorCount(), result.getSkippedCount()));
            }

            return "redirect:/admin/students/bulk-upload-result";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/students/bulk-upload";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error processing file: " + e.getMessage());
            return "redirect:/admin/students/bulk-upload";
        }
    }

    @GetMapping("/bulk-upload-result")
    public String bulkUploadResult(Model model) {
        model.addAttribute("pageTitle", "Bulk Upload Result");
        return "admin/students/bulk-upload-result";
    }

    @GetMapping("/download-template")
    @ResponseBody
    public String downloadTemplate() {
        return studentService.getCSVTemplate();
    }

    @GetMapping("/download-template-excel")
    public ResponseEntity<byte[]> downloadExcelTemplate() {
        byte[] excelContent = studentService.getExcelTemplate();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students_template.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    @GetMapping
    public String listStudents(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(required = false) String keyword,
                               Model model) {
        int pageSize = 20;
        Page<Student> studentPage = studentService.getAllStudents(page, pageSize, keyword);

        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("totalItems", studentPage.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Manage Students");

        return "admin/students/list";
    }
}