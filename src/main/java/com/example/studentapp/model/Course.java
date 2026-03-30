package com.example.studentapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Entity representing a Course in the university system.
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    @Column(nullable = false)
    private String courseName;

    @NotBlank(message = "Course code is required")
    @Column(nullable = false, unique = true)
    private String courseCode;

    @NotBlank(message = "Lecturer name is required")
    @Column(nullable = false)
    private String lecturer;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits cannot exceed 6")
    @Column(nullable = false)
    private Integer credits;

    // One course can have many registrations
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Course() {}

    public Course(String courseName, String courseCode, String lecturer, Integer credits) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.lecturer = lecturer;
        this.credits = credits;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getLecturer() { return lecturer; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }

    @Override
    public String toString() {
        return "Course{id=" + id + ", courseCode='" + courseCode + "', courseName='" + courseName + "'}";
    }
}
