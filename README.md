# Student Course Registration and Management System

**Course:** COMP467 – Advanced Web Applications Programming  
**Technologies:** Spring Boot · Spring MVC · Spring Data JPA · Thymeleaf · H2 · Bootstrap 5 
* Wafula Nelson CS/MK/1218/09/23 
* Ellis Chege CS/MK/0986/09/23

---

## Project Description

UniManage is a full-stack university management web application that allows administrators to manage student records and course catalogues, and enables students to register for or drop courses each semester.

### Key Features
- **Student Management** – Add, view, edit, delete students and view individual profiles
- **Course Management** – Add, view, edit, delete courses with unique course codes
- **Course Registration** – Register students for courses per semester, drop courses, view all enrolled courses per student
- **Dashboard** – Summary statistics: total students, courses, and registrations
- **Validation** – Server-side field validation with user-friendly inline error messages
- **Duplicate Prevention** – Cannot register the same student for the same course in the same semester twice

---

## Project Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- IntelliJ IDEA or Eclipse (recommended)

### Running the Application (H2 – Default)
1. Clone or unzip the project
2. Open the project in IntelliJ IDEA or Eclipse
3. Run `StudentApplication.java` (the main class)
4. Visit: [http://localhost:9090](http://localhost:8080)

The app uses an **H2 in-memory database** by default — no setup needed. Data resets on each restart.

**H2 Console** (inspect the database directly):  
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
- JDBC URL: `jdbc:h2:mem:studentdb`
- Username: `sa` | Password: *(leave blank)*

### Switching to MySQL
1. Open `src/main/resources/application.properties`
2. Comment out the H2 block and uncomment the MySQL block
3. Update username/password to match your local MySQL
4. Create the database: `CREATE DATABASE studentdb;`
5. In `pom.xml`, comment out the H2 dependency and uncomment the MySQL connector
6. Restart the application — Hibernate will auto-create the tables

---

## System Architecture

The application follows the standard **Spring Boot MVC layered architecture**:

```
HTTP Request
     │
     ▼
┌─────────────────────┐
│   Controller Layer   │  Handles HTTP requests, calls service layer
│  (Spring MVC)        │  StudentController, CourseController, RegistrationController
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│   Service Layer      │  Business logic, validation, duplicate checks
│                      │  StudentService, CourseService, RegistrationService
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│  Repository Layer    │  Spring Data JPA interfaces for DB access
│  (Spring Data JPA)   │  StudentRepository, CourseRepository, RegistrationRepository
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│   Entity / Model     │  JPA entities mapped to database tables
│   Layer              │  Student, Course, Registration
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│    Database          │  H2 (dev) or MySQL (production)
└─────────────────────┘
         │
         ▼ (data returned up through layers)
┌─────────────────────┐
│    View Layer        │  Thymeleaf HTML templates + Bootstrap 5
│   (Thymeleaf)        │  Rendered and returned to the browser
└─────────────────────┘
```

### Database ERD

```
students                courses
─────────────────       ──────────────────
id (PK)                 id (PK)
name                    courseName
email (unique)          courseCode (unique)
program                 lecturer
yearOfStudy             credits
                            │
        └──────────┬────────┘
                   │
             registrations
             ──────────────────
             id (PK)
             student_id (FK → students.id)
             course_id  (FK → courses.id)
             semester
             UNIQUE(student_id, course_id, semester)
```

---

## Innovation & Unique Aspects

- **Total Credits Footer** – The student's registered courses page automatically sums and displays total enrolled credits using Thymeleaf's `#aggregates.sum()`
- **Duplicate Registration Guard** – A database-level unique constraint plus service-layer validation prevents double-enrollment
- **Quick Actions Dashboard** – The homepage provides one-click access to common admin tasks
- **Profile Avatars** – Student initials are extracted and displayed as styled avatar circles — no image uploads needed
- **Bootstrap 5 + Bootstrap Icons** – Polished, responsive UI that works on desktop and mobile
- **Pre-selected Student on Registration Form** – Navigating to register from a student's profile pre-fills the student dropdown

---

## Challenges Encountered

1. **Lazy Loading with Thymeleaf** – JPA lazy-loaded associations (`@OneToMany`) initially caused `LazyInitializationException` errors when Thymeleaf tried to render related entities. Resolved by ensuring the session was open during rendering and by using `FetchType.LAZY` carefully with explicit service method calls.

2. **Cascade Delete** – Deleting a student or course with existing registrations initially violated foreign key constraints. Resolved using `CascadeType.ALL` and `orphanRemoval = true` on the entity relationships.

3. **Duplicate Email/Code Validation** – Standard `@UniqueConstraint` throws a low-level database error rather than a user-friendly message. Resolved by implementing explicit service-layer checks before saving.

4. **Thymeleaf Form Binding on Edit** – Pre-populating select dropdowns (e.g., year, credits) required correct use of `th:field` and `th:selected` to match existing entity values.

---

## Lessons Learned

- **Separation of concerns** is critical — keeping business logic in the service layer keeps controllers clean and testable
- **Spring Data JPA** dramatically reduces boilerplate — derived query methods like `existsByEmailAndIdNot()` generate SQL automatically
- **Bean Validation** combined with `BindingResult` provides a seamless user experience for form validation
- **Thymeleaf's** `th:object`, `th:field`, and `th:errors` work as a cohesive system — breaking any one piece causes silent failures
- **Flash attributes** (`RedirectAttributes.addFlashAttribute`) are the correct way to pass success/error messages across a redirect

---

## Project Structure

```
studentapp/
├── pom.xml
└── src/main/
    ├── java/com/example/studentapp/
    │   ├── StudentApplication.java
    │   ├── controller/
    │   │   ├── DashboardController.java
    │   │   ├── StudentController.java
    │   │   ├── CourseController.java
    │   │   └── RegistrationController.java
    │   ├── service/
    │   │   ├── StudentService.java
    │   │   ├── CourseService.java
    │   │   └── RegistrationService.java
    │   ├── repository/
    │   │   ├── StudentRepository.java
    │   │   ├── CourseRepository.java
    │   │   └── RegistrationRepository.java
    │   └── model/
    │       ├── Student.java
    │       ├── Course.java
    │       └── Registration.java
    └── resources/
        ├── application.properties
        └── templates/
            ├── dashboard.html
            ├── students/
            │   ├── students.html
            │   ├── add-student.html
            │   ├── edit-student.html
            │   └── student-profile.html
            ├── courses/
            │   ├── courses.html
            │   ├── add-course.html
            │   └── edit-course.html
            └── registrations/
                ├── registration-students.html
                ├── register-course.html
                └── student-courses.html
```

---

## Group Members

| Name | Student ID |
|------|------------|
| *(Add your names here)* | |

---

*Prepared for COMP467 Advanced Web Applications Programming*
