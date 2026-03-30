package com.example.studentapp.service;

import com.example.studentapp.model.Student;
import com.example.studentapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Student business logic.
 * Handles validation and delegates persistence to StudentRepository.
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieve all students from the database.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Find a student by ID.
     * Throws an exception if not found.
     */
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

    /**
     * Save a new student record.
     * Validates that the email is not already in use.
     */
    public Student saveStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("A student with this email already exists.");
        }
        return studentRepository.save(student);
    }

    /**
     * Update an existing student record.
     * Validates that the updated email is not taken by another student.
     */
    public Student updateStudent(Student student) {
        if (studentRepository.existsByEmailAndIdNot(student.getEmail(), student.getId())) {
            throw new IllegalArgumentException("Another student is already using this email.");
        }
        return studentRepository.save(student);
    }

    /**
     * Delete a student by their ID.
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    /**
     * Return the total count of students.
     */
    public long countStudents() {
        return studentRepository.count();
    }

    /**
     * Check if a student exists by ID.
     */
    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }
}
