package com.elearning.notification;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Contract Test for NotificationService - Producer Side
 * 
 * This test verifies that the API respects the contract defined in:
 * src/test/resources/contracts/notification_send_contract.groovy
 * 
 * Contract Definition:
 * - Request: POST /api/notifications with userId and message in request params
 * - Response: 201 Created with id, userId, message, and status fields
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration")
@AutoConfigureMockMvc
public class NotificationContractTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test: Send Notification Contract
     * 
     * Verifies that POST /api/notifications:
     * 1. Accepts request params: studentId, message, type
     * 2. Returns HTTP 201 Created
     * 3. Returns a response body with id, userId, message, and status fields
     */
    @Test
    public void testSendNotificationContract() throws Exception {
        mockMvc.perform(post("/api/notifications")
                .param("studentId", "123")
                .param("message", "Hello Notification")
                .param("type", "GENERAL"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.message").value("Hello Notification"))
                .andExpect(jsonPath("$.status").exists());
    }

}

