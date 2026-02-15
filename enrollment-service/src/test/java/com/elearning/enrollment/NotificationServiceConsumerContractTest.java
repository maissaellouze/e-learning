package com.elearning.enrollment;

import com.elearning.enrollment.client.NotificationResponse;
import com.elearning.enrollment.client.NotificationServiceClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Consumer-Side Contract Test for EnrollmentService
 * 
 * This test demonstrates how the consumer (EnrollmentService) can test against
 * the NotificationService API without actually starting it.
 * 
 * We use WireMock to simulate the NotificationService responses based on the contract.
 * The contract defines:
 * - Request: POST /api/notifications with studentId, message, type params
 * - Response: 201 Created with id, userId, message, status fields
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationServiceConsumerContractTest {

    private static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    @BeforeAll
    public static void startWireMock() {
        wireMockServer.start();
    }

    @BeforeEach
    public void resetWireMock() {
        // ensure each test starts with a clean slate (no previous requests or stubs)
        wireMockServer.resetRequests();
        wireMockServer.resetMappings();
    }

    @AfterAll
    public static void stopWireMock() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("notification-service.url", () -> "http://localhost:" + wireMockServer.port());
    }

    /**
     * Test: Consumer calls NotificationService to send notification
     * 
     * This test verifies the contract from the consumer side:
     * 1. Consumer sends POST /api/notifications with studentId, message, and type
     * 2. WireMock (stub) responds with 201 and notification data
     * 3. Consumer successfully processes the response
     */
    @Test
    public void testConsumerCallsNotificationServiceContract() {
        // Arrange: Setup WireMock stub for POST /api/notifications
        // This simulates the NotificationService's response per the contract
        wireMockServer.stubFor(post(urlPathEqualTo("/api/notifications"))
                .withQueryParam("studentId", equalTo("123"))
                .withQueryParam("message", equalTo("Enrollment Notification"))
                .withQueryParam("type", equalTo("INFO"))
                .willReturn(aResponse()
                    .withStatus(201)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                            "id": 1,
                            "userId": 123,
                            "message": "Enrollment Notification",
                            "status": "SENT"
                        }
                        """)));

        // Act: Call the notification service via REST client
        ResponseEntity<NotificationResponse> response = notificationServiceClient.sendNotification(
                123L,
                "Enrollment Notification",
                "INFO");

        // Assert: Verify the response matches the contract
        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(123L, response.getBody().getUserId());
        assertEquals("Enrollment Notification", response.getBody().getMessage());
        assertEquals("SENT", response.getBody().getStatus());

        // Verify the stub was called exactly once
        wireMockServer.verify(1, postRequestedFor(urlPathEqualTo("/api/notifications")));
    }

    /**
     * Test: Consumer handles error responses from NotificationService
     * 
     * This test verifies that the consumer can handle error cases
     * (e.g., 500 Internal Server Error)
     */
    @Test
    public void testConsumerHandlesNotificationServiceError() {
        // Arrange: Setup WireMock stub to return 500 error
        wireMockServer.stubFor(post(urlPathEqualTo("/api/notifications"))
                .willReturn(aResponse()
                    .withStatus(500)
                    .withHeader("Content-Type", "application/json")
                    .withBody("""
                        {
                            "error": "Internal Server Error"
                        }
                        """)));

        // Act & Assert: Verify that the call fails gracefully
        assertThrows(Exception.class, () -> {
            notificationServiceClient.sendNotification(
                    123L,
                    "Test Message",
                    "INFO");
        });
    }

}
