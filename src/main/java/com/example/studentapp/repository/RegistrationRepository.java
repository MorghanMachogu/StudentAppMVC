package com.example.studentapp.repository;

import com.example.studentapp.model.Registration;
import com.example.studentapp.model.Student;
import com.example.studentapp.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Registration CRUD operations via Spring Data JPA.
 */
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /** Get all registrations for a specific student. */
    List<Registration> findByStudent(Student student);

    /** Get all registrations for a specific course. */
    List<Registration> findByCourse(Course course);

    /** Check if a student is already registered for a course in a given semester. */
    boolean existsByStudentAndCourseAndSemester(Student student, Course course, String semester);

    /** Find a specific registration by student, course, and semester. */
    Registration findByStudentAndCourseAndSemester(Student student, Course course, String semester);

    /** Count how many times a course appears in registrations. */
    long countByCourse(Course course);
}
