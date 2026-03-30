package com.example.studentapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Entity representing a Course Registration linking a Student to a Course.
 */
@Entity
@Table(
    name = "registrations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id", "semester"})
)
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many registrations belong to one student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Many registrations belong to one course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Semester is required")
    @Column(nullable = false)
    private String semester;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Registration() {}

    public Registration(Student student, Course course, String semester) {
        this.student = student;
        this.course = course;
        this.semester = semester;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    @Override
    public String toString() {
        return "Registration{id=" + id + ", studentId=" + (student != null ? student.getId() : null)
                + ", courseId=" + (course != null ? course.getId() : null)
                + ", semester='" + semester + "'}";
    }
}
