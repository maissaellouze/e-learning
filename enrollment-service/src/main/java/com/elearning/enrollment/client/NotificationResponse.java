package com.elearning.enrollment.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response DTO from NotificationService
 * Represents the notification returned by the producer service
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String message;
    private String status;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, Long userId, String message, String status) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
