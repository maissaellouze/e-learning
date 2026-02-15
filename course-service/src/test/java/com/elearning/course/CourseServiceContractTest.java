package com.elearning.course;

import com.elearning.common.dto.CourseDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseServiceContractTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    public void testCreateCourse() {
        CourseDTO courseDTO = CourseDTO.builder()
                .title("Spring Boot Microservices")
                .description("Learn microservices with Spring Boot")
                .instructor("John Doe")
                .build();

        given()
                .contentType("application/json")
                .body(courseDTO)
                .when()
                .post("/api/courses")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", equalTo("Spring Boot Microservices"))
                .body("instructor", equalTo("John Doe"));
    }

    @Test
    public void testGetAllCourses() {
        given()
                .when()
                .get("/api/courses")
                .then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class));
    }

    @Test
    public void testGetCourseById() {
        CourseDTO courseDTO = CourseDTO.builder()
                .title("Advanced Java")
                .description("Master advanced Java concepts")
                .instructor("Jane Smith")
                .build();

        // Create course first
        int courseId = given()
                .contentType("application/json")
                .body(courseDTO)
                .when()
                .post("/api/courses")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Get the course
        given()
                .when()
                .get("/api/courses/" + courseId)
                .then()
                .statusCode(200)
                .body("id", equalTo(courseId))
                .body("title", equalTo("Advanced Java"));
    }

    @Test
    public void testCreateModule() {
        CourseDTO courseDTO = CourseDTO.builder()
                .title("Web Development")
                .description("Learn web development")
                .instructor("Bob Wilson")
                .build();

        // Create course
        int courseId = given()
                .contentType("application/json")
                .body(courseDTO)
                .when()
                .post("/api/courses")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Create module
        com.elearning.course.dto.ModuleDTO moduleDTO = com.elearning.course.dto.ModuleDTO.builder()
                .name("Introduction to HTML")
                .description("Learn HTML basics")
                .build();

        given()
                .contentType("application/json")
                .body(moduleDTO)
                .when()
                .post("/api/courses/" + courseId + "/modules")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Introduction to HTML"))
                .body("courseId", equalTo(courseId));
    }
}
