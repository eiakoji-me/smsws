package com.github.eiakojime.courseservice;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

@SpringBootApplication
@EnableDiscoveryClient
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
	private final CourseMessageProducer courseMessageProducer;
	private int numberOfAttempts = 0;

	@GetMapping
//	@CircuitBreaker(name="courseBreaker", fallbackMethod = "findAllFallback")
//	@Retry(name="courseRetry", fallbackMethod = "findAllFallback")
	@RateLimiter(name = "courseRateLimiter", fallbackMethod = "courseRateLimiterFallback")
	Iterable<Course> findAll() {
		//throw new InternalServerErrorException();
		Iterable<Course> courses = courseRepository.findAll();
		StreamSupport
				.stream(courses.spliterator(),false)
				.peek(System.out::println)
				.forEach(courseMessageProducer::sendMessage);
		return courses;
	}

	public Iterable<Course> findAllFallback(Exception e) {
		return new ArrayList<>();
	}

	public Iterable<Course> courseRateLimiterFallback(Exception e){
		System.out.println("Finding all courses, Request denied with: %s".formatted(e.getMessage()));
		return findAllFallback(e);
	}

	@GetMapping("/{id}")
	Course findById(@PathVariable long id) {
		Course course = courseRepository.findById(id).orElse(null);
		Assert.notNull(course, "Course with id: %s not found".formatted(id));
		courseMessageProducer.sendMessage(course);
		return course;
	}

	@GetMapping("/name/{name}")
	Iterable<Course> findByName(@PathVariable String name) {
		Assert.state(Character.isUpperCase(name.charAt(0)), "The name must start with an upper case!");
		return courseRepository.findByNameContains(name);
	}

	@GetMapping("/department/{name}")
	Iterable<Course> findByDepartment(@PathVariable String name) {
		Assert.state(Character.isUpperCase(name.charAt(0)), "The name must start with an upper case!");
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

@Configuration
class RabbitMQConfiguration {
	@Bean
	public Queue courseQueue() {
		return new Queue("courseQueue");
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		return rabbitTemplate;
	}
}

@RequiredArgsConstructor
@Service
class CourseMessageProducer {
	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(Course course) {
		rabbitTemplate.convertAndSend("courseQueue", course);
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

	@ExceptionHandler
	ProblemDetail handle(RequestNotPermitted requestNotPermitted) {
		var pd = ProblemDetail.forStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		pd.setDetail(requestNotPermitted.getLocalizedMessage());
		return pd;
	}
}