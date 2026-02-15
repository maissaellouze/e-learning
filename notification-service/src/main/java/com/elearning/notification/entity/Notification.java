package com.elearning.notification.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String message;

    @Column(length = 500)
    private String details;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_read")
    private Boolean isRead = false;

    public Notification() {
    }

    public Notification(Long studentId, String message, String details, NotificationType type) {
        this.studentId = studentId;
        this.message = message;
        this.details = details;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public static class NotificationBuilder {
        private Long studentId;
        private String message;
        private String details;
        private NotificationType type;
        private Boolean isRead = false;

        public NotificationBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public NotificationBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationBuilder details(String details) {
            this.details = details;
            return this;
        }

        public NotificationBuilder type(NotificationType type) {
            this.type = type;
            return this;
        }

        public NotificationBuilder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification(studentId, message, details, type);
            notification.isRead = isRead;
            return notification;
        }
    }
}
