package com.example.studentapp.controller;

import com.example.studentapp.model.Course;
import com.example.studentapp.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Course management (CRUD).
 * Handles all /courses/** routes.
 */
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ── LIST ALL COURSES ──────────────────────────────────────────────────────

    /**
     * GET /courses  → display all courses.
     */
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses/courses";
    }

    // ── ADD COURSE ────────────────────────────────────────────────────────────

    /**
     * GET /courses/add  → show the add-course form.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        return "courses/add-course";
    }

    /**
     * POST /courses/add  → process form submission to create a course.
     */
    @PostMapping("/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "courses/add-course";
        }

        try {
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
            return "redirect:/courses";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "courses/add-course";
        }
    }

    // ── EDIT COURSE ───────────────────────────────────────────────────────────

    /**
     * GET /courses/edit/{id}  → show the edit form pre-filled.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("course", courseService.getCourseById(id));
            return "courses/edit-course";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/courses";
        }
    }

    /**
     * POST /courses/edit/{id}  → process updated course data.
     */
    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                                @Valid @ModelAttribute("course") Course course,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        course.setId(id);

        if (bindingResult.hasErrors()) {
            return "courses/edit-course";
        }

        try {
            courseService.updateCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
            return "redirect:/courses";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "courses/edit-course";
        }
    }

    // ── DELETE COURSE ─────────────────────────────────────────────────────────

    /**
     * POST /courses/delete/{id}  → delete a course by ID.
     */
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/courses";
    }
}
