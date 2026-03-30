package com.example.studentapp.service;

import com.example.studentapp.model.Course;
import com.example.studentapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Course business logic.
 * Handles validation and delegates persistence to CourseRepository.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieve all courses from the database.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Find a course by ID.
     * Throws an exception if not found.
     */
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + id));
    }

    /**
     * Save a new course record.
     * Validates that the course code is unique.
     */
    public Course saveCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new IllegalArgumentException("A course with this code already exists.");
        }
        return courseRepository.save(course);
    }

    /**
     * Update an existing course.
     * Validates that the updated course code is not taken by another course.
     */
    public Course updateCourse(Course course) {
        if (courseRepository.existsByCourseCodeAndIdNot(course.getCourseCode(), course.getId())) {
            throw new IllegalArgumentException("Another course is already using this code.");
        }
        return courseRepository.save(course);
    }

    /**
     * Delete a course by its ID.
     */
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    /**
     * Return the total count of courses.
     */
    public long countCourses() {
        return courseRepository.count();
    }
}
