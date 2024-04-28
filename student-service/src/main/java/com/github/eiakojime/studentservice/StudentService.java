package com.github.eiakojime.studentservice;

import jakarta.validation.constraints.Email;
import jakarta.websocket.server.PathParam;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
class StudentControllerRest {
    private final StudentRepository studentRepository;

    @GetMapping
    Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    Student findById(@PathVariable UUID id) {
        Student student = studentRepository.findById(id).orElse(null);
        Assert.notNull(student, "Student with id: %s not found".formatted(id));

        return student;
    }

    @GetMapping("/email/{email}")
    Iterable<Student> findByEmail(@PathVariable String email) {
        Assert.state(EmailValidator.getInstance().isValid(email), "Email format must be valid");
        return studentRepository.findByEmail(email);
    }

    @GetMapping("/name")
    Iterable<Student> findByName(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName ) {
        return studentRepository.findByFirstNameOrLastName(firstName, lastName);
    }
}

@SpringBootApplication
public class StudentService {

    public static void main(String[] args) {
        SpringApplication.run(StudentService.class, args);
    }

}

interface StudentRepository extends CrudRepository<Student, UUID> {
    Iterable<Student> findByFirstNameOrLastName(String firstName, String lastName);

    Iterable<Student> findByEmail(String email);
}

@Builder
record Student(
        @Id UUID id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        String address,
        @Email
        @UniqueElements
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {
}

enum Gender {
    MALE("male"),
    FEMALE("female");
    private final String value;

    Gender(String value) {
        this.value = value;
    }
}

@RestControllerAdvice
class GlobalExceptionHandlers {
    @ExceptionHandler
    ProblemDetail handle(IllegalStateException illegalStateException) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        pd.setDetail(illegalStateException.getLocalizedMessage());
        return pd;
    }

    @ExceptionHandler
    ProblemDetail handle(IllegalArgumentException illegalArgumentException) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
        pd.setDetail(illegalArgumentException.getLocalizedMessage());
        return pd;
    }
}