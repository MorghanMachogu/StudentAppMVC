package com.example.studentapp.repository;

import com.example.studentapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Student CRUD operations via Spring Data JPA.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /** Check if a student with the given email already exists (for validation). */
    boolean existsByEmail(String email);

    /** Check if another student (different ID) already uses this email. */
    boolean existsByEmailAndIdNot(String email, Long id);

    /** Find a student by their email address. */
    Optional<Student> findByEmail(String email);
}
