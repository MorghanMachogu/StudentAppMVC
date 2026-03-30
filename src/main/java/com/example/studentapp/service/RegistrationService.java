package com.example.studentapp.service;

import com.example.studentapp.model.Course;
import com.example.studentapp.model.Registration;
import com.example.studentapp.model.Student;
import com.example.studentapp.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Registration business logic.
 * Handles duplicate prevention, drop validation, and delegates to RegistrationRepository.
 */
@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    /**
     * Register a student for a course in a given semester.
     * Prevents duplicate registrations for the same course and semester.
     */
    public Registration registerStudentForCourse(Student student, Course course, String semester) {
        if (registrationRepository.existsByStudentAndCourseAndSemester(student, course, semester)) {
            throw new IllegalArgumentException(
                    student.getName() + " is already registered for " +
                    course.getCourseName() + " in " + semester + "."
            );
        }
        Registration registration = new Registration(student, course, semester);
        return registrationRepository.save(registration);
    }

    /**
     * Get all courses registered by a specific student.
     */
    public List<Registration> getRegistrationsByStudent(Student student) {
        return registrationRepository.findByStudent(student);
    }

    /**
     * Get all registrations for a specific course.
     */
    public List<Registration> getRegistrationsByCourse(Course course) {
        return registrationRepository.findByCourse(course);
    }

    /**
     * Drop (delete) a registration by its ID.
     * Throws an exception if the registration does not exist.
     */
    public void dropRegistration(Long registrationId) {
        if (!registrationRepository.existsById(registrationId)) {
            throw new RuntimeException("Registration not found with ID: " + registrationId);
        }
        registrationRepository.deleteById(registrationId);
    }

    /**
     * Get all registrations (used for dashboard count).
     */
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    /**
     * Total registration count for the dashboard.
     */
    public long countRegistrations() {
        return registrationRepository.count();
    }
}
