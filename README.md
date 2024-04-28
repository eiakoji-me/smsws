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

## Services

- Schools service
- Courses service
- Students service

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

## Useful resources

- Visit [mark down badges](https://ileriayo.github.io/markdown-badges/) to add badges related to tech stack for this repo