package com.elearning.gateway.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("")
public class GatewayController {

    private final WebClient webClient;

    public GatewayController(WebClient webClient) {
        this.webClient = webClient;
    }

    // Auth routes
    @PostMapping("/auth/register")
    public Mono<ResponseEntity<Object>> register(@RequestBody Object request) {
        return webClient.post()
                .uri("http://localhost:8081/auth/register")
                .bodyValue(request)
                .retrieve()
                .toEntity(Object.class)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/auth/login")
    public Mono<ResponseEntity<Object>> login(@RequestBody Object request) {
        return webClient.post()
                .uri("http://localhost:8081/auth/login")
                .bodyValue(request)
                .retrieve()
                .toEntity(Object.class)
                .map(ResponseEntity::ok);
    }

    // Course routes
    @GetMapping("/api/courses")
    public Mono<ResponseEntity<Object>> getCourses() {
        return webClient.get()
                .uri("http://localhost:8082/courses")
                .retrieve()
                .toEntity(Object.class)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/api/courses")
    public Mono<ResponseEntity<Object>> createCourse(@RequestBody Object request) {
        return webClient.post()
                .uri("http://localhost:8082/courses")
                .bodyValue(request)
                .retrieve()
                .toEntity(Object.class)
                .map(ResponseEntity::ok);
    }

    // Enrollment routes
    @GetMapping("/api/enrollments")
    public Mono<ResponseEntity<Object>> getEnrollments() {
        return webClient.get()
                .uri("http://localhost:8083/enrollments")
                .retrieve()
                .toEntity(Object.class)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/api/enrollments")
    public Mono<ResponseEntity<Object>> createEnrollment(@RequestBody Object request) {
        return webClient.post()
                .uri("http://localhost:8083/enrollments")
                .bodyValue(request)
                .retrieve()
                .toEntity(Object.class)
                .map(ResponseEntity::ok);
    }

    // Notification routes
    @GetMapping("/api/notifications")
    public Mono<ResponseEntity<Object>> getNotifications() {
        return webClient.get()
                .uri("http://localhost:8084/notifications")
                .retrieve()
                .toEntity(Object.class)
                .map(ResponseEntity::ok);
    }

}
