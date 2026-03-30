package com.example.studentapp.controller;

import com.example.studentapp.model.Student;
import com.example.studentapp.service.CourseService;
import com.example.studentapp.service.RegistrationService;
import com.example.studentapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Course Registration operations.
 * Handles /registrations/** routes.
 */
@Controller
@RequestMapping("/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public RegistrationController(RegistrationService registrationService,
                                   StudentService studentService,
                                   CourseService courseService) {
        this.registrationService = registrationService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // ── REGISTRATION FORM ─────────────────────────────────────────────────────

    /**
     * GET /registrations/register  → show the course registration form.
     * Pre-selects student if studentId is passed as a query param.
     */
    @GetMapping("/register")
    public String showRegistrationForm(@RequestParam(required = false) Long studentId, Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("selectedStudentId", studentId);
        model.addAttribute("semesters", new String[]{
            "Semester 1 - 2024/2025",
            "Semester 2 - 2024/2025",
            "Semester 1 - 2025/2026",
            "Semester 2 - 2025/2026"
        });
        return "registrations/register-course";
    }

    /**
     * POST /registrations/register  → process course registration form.
     */
    @PostMapping("/register")
    public String registerCourse(@RequestParam Long studentId,
                                  @RequestParam Long courseId,
                                  @RequestParam String semester,
                                  RedirectAttributes redirectAttributes) {
        try {
            var student = studentService.getStudentById(studentId);
            var course = courseService.getCourseById(courseId);
            registrationService.registerStudentForCourse(student, course, semester);
            redirectAttributes.addFlashAttribute("successMessage",
                    student.getName() + " successfully registered for " + course.getCourseName() + "!");
            return "redirect:/registrations/student/" + studentId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/registrations/register?studentId=" + studentId;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/registrations/register";
        }
    }

    // ── VIEW STUDENT'S REGISTERED COURSES ─────────────────────────────────────

    /**
     * GET /registrations/student/{studentId}  → view all courses a student is registered for.
     */
    @GetMapping("/student/{studentId}")
    public String viewStudentRegistrations(@PathVariable Long studentId,
                                            Model model,
                                            RedirectAttributes redirectAttributes) {
        try {
            Student student = studentService.getStudentById(studentId);
            var registrations = registrationService.getRegistrationsByStudent(student);
            model.addAttribute("student", student);
            model.addAttribute("registrations", registrations);
            return "registrations/student-courses";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/students";
        }
    }

    // ── DROP COURSE ───────────────────────────────────────────────────────────

    /**
     * POST /registrations/drop/{registrationId}  → drop (delete) a course registration.
     */
    @PostMapping("/drop/{registrationId}")
    public String dropCourse(@PathVariable Long registrationId,
                              @RequestParam Long studentId,
                              RedirectAttributes redirectAttributes) {
        try {
            registrationService.dropRegistration(registrationId);
            redirectAttributes.addFlashAttribute("successMessage", "Course dropped successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to drop course: " + e.getMessage());
        }
        return "redirect:/registrations/student/" + studentId;
    }

    // ── SELECT STUDENT (list all students to pick one) ────────────────────────

    /**
     * GET /registrations  → show student list for selecting who to manage registrations for.
     */
    @GetMapping
    public String listStudentsForRegistration(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "registrations/registration-students";
    }
}
