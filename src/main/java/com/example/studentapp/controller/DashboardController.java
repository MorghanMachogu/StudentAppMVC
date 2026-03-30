package com.example.studentapp.controller;

import com.example.studentapp.service.CourseService;
import com.example.studentapp.service.RegistrationService;
import com.example.studentapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the application dashboard (homepage).
 * Displays summary statistics: total students, courses, and registrations.
 */
@Controller
public class DashboardController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final RegistrationService registrationService;

    @Autowired
    public DashboardController(StudentService studentService,
                                CourseService courseService,
                                RegistrationService registrationService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.registrationService = registrationService;
    }

    /**
     * GET /  → renders the dashboard homepage.
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentService.countStudents());
        model.addAttribute("totalCourses", courseService.countCourses());
        model.addAttribute("totalRegistrations", registrationService.countRegistrations());
        return "dashboard";
    }
}
