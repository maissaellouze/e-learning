package com.elearning.notification.dto;

public class NotificationDTO {
    private Long id;
    private Long studentId;
    private String message;
    private String details;
    private String type;
    private String createdAt;
    private Boolean isRead;
    private String status;

    public NotificationDTO() {
    }

    public NotificationDTO(Long id, Long studentId, String message, String details, String type, String createdAt, Boolean isRead) {
        this.id = id;
        this.studentId = studentId;
        this.message = message;
        this.details = details;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public static NotificationDTOBuilder builder() {
        return new NotificationDTOBuilder();
    }

    public static class NotificationDTOBuilder {
        private Long id;
        private Long studentId;
        private String message;
        private String details;
        private String type;
        private String createdAt;
        private Boolean isRead;
        private String status;

        public NotificationDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public NotificationDTOBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public NotificationDTOBuilder message(String message) {
            this.message = message;
            return this;
        }

        public NotificationDTOBuilder details(String details) {
            this.details = details;
            return this;
        }

        public NotificationDTOBuilder type(String type) {
            this.type = type;
            return this;
        }

        public NotificationDTOBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NotificationDTOBuilder isRead(Boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public NotificationDTOBuilder status(String status) {
            this.status = status;
            return this;
        }

        public NotificationDTO build() {
            NotificationDTO dto = new NotificationDTO(id, studentId, message, details, type, createdAt, isRead);
            dto.setStatus(this.status);
            return dto;
        }
    }
}
