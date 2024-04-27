package com.github.eiakojime.courseservice;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@SpringBootApplication
public class CourseService {

	public static void main(String[] args) {
		SpringApplication.run(CourseService.class, args);
	}

}

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
class CourseControllerRest {
	private final CourseRepository courseRepository;

	@GetMapping
	Iterable<Course> findAll() {
		return courseRepository.findAll();
	}

	@GetMapping("/{id}")
	Course findById(@PathVariable long id) {
		Course course = courseRepository.findById(id).orElse(null);
		Assert.notNull(course, "Course with id: %s not found".formatted(id));

		return course;
	}

	@GetMapping("/name/{name}")
	Iterable<Course> findByName(@PathVariable String name) {
		//Assert.state(Character.isUpperCase(name.charAt(0)), "The name must start with an upper case!");
		return courseRepository.findByNameContains(name);
	}

	@GetMapping("/department/{name}")
	Iterable<Course> findByDepartment(@PathVariable String name) {
		//Assert.state(Character.isUpperCase(name.charAt(0)), "The name must start with an upper case!");
		return courseRepository.findByDepartmentContains(name);
	}
}

interface CourseRepository extends CrudRepository<Course, Long> {
	Iterable<Course> findByNameContains(String name);
	Iterable<Course> findByDepartmentContains(String department);
}

@Builder
record Course(
		@Id Long id,
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