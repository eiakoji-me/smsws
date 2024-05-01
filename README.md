![Shell Script](https://img.shields.io/badge/shell_script-%23121011.svg?logo=gnu-bash&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=Gradle&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![Spring IO](https://img.shields.io/badge/Spring-6DB33F?logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?&logo=Hibernate&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?&logo=docker&logoColor=white)
![Postgres](https://img.shields.io/badge/PostgreSQL-316192?logo=postgresql&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?logo=githubactions&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?e&logo=amazon-aws&logoColor=white)
![Terraform](https://img.shields.io/badge/terraform-%235835CC.svg?logo=terraform&logoColor=white)

<h1 align="center">School Management App</h1>
<p>In this project, our primary focus is to explore the core concepts of microservices architecture, leveraging the robust capabilities of the Spring Boot framework in tandem with the Spring Cloud project and associated tools. At the heart of our endeavor lies the demonstration of fundamental microservice principles within the context of a School Management application. Throughout our journey, we delve into a myriad of essential concepts, encompassing diverse areas such as service discovery (utilizing Netflix Eureka), proficiently handling rest clients through openfeign, RestTemplate, Java Reactive WebClient, and GraphQL.</p> 

<p>Additionally, we delve into advanced topics including distributed tracing, metrics collection, configuration management with config servers, API gateway implementation, and the circuit breaker pattern integrated with resilience4j. Moreover, our exploration extends to incorporate pivotal elements like message brokering with RabbitMQ, event streaming utilizing Kafka, OAuth2 authentication for secure access, GraalVM for building native images, Swagger OpenAPI documentation for REST endpoints, and much more. Through this comprehensive endeavor, we aim to gain a profound understanding of microservices architecture and its practical application, equipping ourselves with the tools and knowledge necessary to architect resilient and scalable distributed systems.</p>

## Services

- [Schools service](https://github.com/eiakoji-me/smsws/tree/develop/school-service)
- [Courses service](https://github.com/eiakoji-me/smsws/tree/develop/course-service)
- [Students service](https://github.com/eiakoji-me/smsws/tree/develop/student-service)
- [Registry service](https://github.com/eiakoji-me/smsws/tree/develop/registry-service)

## Setting up Zipkin Server with docker image

The Docker Zipkin project is able to build docker images, provide scripts and a docker-compose.yml for launching pre-built images. The quickest start is to run the latest image directly:

    docker run -d -p 9411:9411 openzipkin/zipkin


## Snipets

**Add a new service**

```bash
spring init --java-version=21 \
--build=gradle \
--packaging=jar \
--type=gradle-project \
--artifact-id=school-service \
--group-id=com.github.eiakojime \
--dependencies=actuator,data-jdbc,postgresql,web \
--extract school-service
```

## Environment variables

```
PROJECT_ROOT = 
```

## Dependencies

- Docker-compose
- Fault tolerance with Resilence4j
- GraalVM
- OpenFeign
- PostgreSQL
- Rest Template with load balancing
- Spring Actuator
- Spring cloud Gateway
- Spring cloud Config Server
- Spring GraphQL
- Spring cloud Eureka
- Zipkin distributed tracing

## Useful resources

- Visit [Mark down badges](https://ileriayo.github.io/markdown-badges/) to view cool badges for your repos
- Visit [Spring CLoud Netflix](https://cloud.spring.io/spring-cloud-netflix/reference/html/) to learn more about Eureka
- Visit [Zipkin Quick start](https://zipkin.io/pages/quickstart) to learn more about zipkin server
- Visit [Micrometer.io](https://micrometer.io/) to learn more on working with metrics
- Visit [https://resilience4j.readme.io/](https://resilience4j.readme.io/) to learn more about Resilience4j
- Visit [https://cloud.spring.io/spring-cloud-gateway/reference/html/](https://cloud.spring.io/spring-cloud-gateway/reference/html/) learn more about spring cloud gateway
- Learn more about RabbitMQ at [https://www.rabbitmq.com/](https://www.rabbitmq.com/)