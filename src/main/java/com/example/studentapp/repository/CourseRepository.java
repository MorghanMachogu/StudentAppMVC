package com.example.studentapp.repository;

import com.example.studentapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Course CRUD operations via Spring Data JPA.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /** Check if a course with the given code already exists. */
    boolean existsByCourseCode(String courseCode);

    /** Check if another course (different ID) already uses this code. */
    boolean existsByCourseCodeAndIdNot(String courseCode, Long id);
}
