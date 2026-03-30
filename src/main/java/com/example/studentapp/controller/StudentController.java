package com.example.studentapp.controller;

import com.example.studentapp.model.Student;
import com.example.studentapp.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Student management (CRUD).
 * Handles all /students/** routes.
 */
@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ── LIST ALL STUDENTS ─────────────────────────────────────────────────────

    /**
     * GET /students  → display all students.
     */
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students/students";
    }

    // ── ADD NEW STUDENT ───────────────────────────────────────────────────────

    /**
     * GET /students/add  → show the add-student form.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "students/add-student";
    }

    /**
     * POST /students/add  → process form submission to create a student.
     */
    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute("student") Student student,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (bindingResult.hasErrors()) {
            return "students/add-student";
        }

        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "students/add-student";
        }
    }

    // ── EDIT STUDENT ──────────────────────────────────────────────────────────

    /**
     * GET /students/edit/{id}  → show the edit form pre-filled with student data.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("student", studentService.getStudentById(id));
            return "students/edit-student";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/students";
        }
    }

    /**
     * POST /students/edit/{id}  → process updated student data.
     */
    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable Long id,
                                 @Valid @ModelAttribute("student") Student student,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        student.setId(id);

        if (bindingResult.hasErrors()) {
            return "students/edit-student";
        }

        try {
            studentService.updateStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
            return "redirect:/students";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "students/edit-student";
        }
    }

    // ── DELETE STUDENT ────────────────────────────────────────────────────────

    /**
     * POST /students/delete/{id}  → delete a student by ID.
     */
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/students";
    }

    // ── VIEW STUDENT PROFILE ──────────────────────────────────────────────────

    /**
     * GET /students/{id}  → view student profile details.
     */
    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("student", studentService.getStudentById(id));
            return "students/student-profile";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/students";
        }
    }
}
