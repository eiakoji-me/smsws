package com.github.eiakojime.schoolservice;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

interface SchoolRepository extends CrudRepository<School, UUID> {
  Iterable<School> findByNameContains(String filter);
}

@FeignClient(name = "COURSE-SERVICE")
@Component
interface CourseClient {
  @GetMapping("/courses")
  Iterable<Course> getAllCourses();

  @GetMapping("/courses/{id}")
  Course getById(@PathVariable("id") long id);
}

@EnableFeignClients
@SpringBootApplication
public class SchoolService {

  public static void main(String[] args) {
    SpringApplication.run(SchoolService.class, args);
  }

  @Bean
  ApplicationRunner init(CourseClient client) {
    return args -> {
      Iterable<Course> courses = client.getAllCourses();
      System.out.println(courses);
    };
  }

}

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
class SchoolControllerRest {
  private final SchoolRepository repository;

  @GetMapping
  Iterable<School> findAll() {
		return repository.findAll();
  }

  @GetMapping("/{id}")
  School findById(@PathVariable UUID id) {
    School school = repository.findById(id).orElse(null);
    Assert.notNull(school, "School with id: %s not found".formatted(id));

    return school;
  }

  @GetMapping("/name/{name}")
  Iterable<School> findByName(@PathVariable String name) {
    Assert.state(Character.isUpperCase(name.charAt(0)), "The name must start with an upper case!");
    return repository.findByNameContains(name);
  }
}

@Builder
record School(@Id UUID id, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
}

@Builder
record Course(
        Long id,
        String name,
        String department,
        Integer credits,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
){}

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