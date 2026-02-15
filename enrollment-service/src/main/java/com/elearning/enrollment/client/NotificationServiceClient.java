package com.elearning.enrollment.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Simple REST client for NotificationService used by EnrollmentService tests.
 * Implemented with RestTemplate to avoid Feign dependency in the test environment.
 */
@Service
public class NotificationServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${notification-service.url:http://localhost:8080}")
    private String baseUrl;

    public ResponseEntity<NotificationResponse> sendNotification(Long studentId, String message, String type) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/notifications")
                .queryParam("studentId", studentId)
                .queryParam("message", message)
                .queryParam("type", type)
                .build().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{}", headers);

        return restTemplate.exchange(uri, HttpMethod.POST, entity, NotificationResponse.class);
    }

}
