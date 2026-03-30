package com.example.studentapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * Entity representing a Student record in the database.
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Program is required")
    @Column(nullable = false)
    private String program;

    @NotNull(message = "Year of study is required")
    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 6, message = "Year cannot exceed 6")
    @Column(nullable = false)
    private Integer yearOfStudy;

    // One student can have many registrations
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Registration> registrations;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Student() {}

    public Student(String name, String email, String program, Integer yearOfStudy) {
        this.name = name;
        this.email = email;
        this.program = program;
        this.yearOfStudy = yearOfStudy;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public Integer getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(Integer yearOfStudy) { this.yearOfStudy = yearOfStudy; }

    public List<Registration> getRegistrations() { return registrations; }
    public void setRegistrations(List<Registration> registrations) { this.registrations = registrations; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }
}
