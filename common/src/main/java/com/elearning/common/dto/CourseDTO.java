package com.elearning.common.dto;

public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private String instructor;
    private Integer totalModules;
    private String createdAt;

    public CourseDTO() {
    }

    public CourseDTO(Long id, String title, String description, String instructor, Integer totalModules, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.totalModules = totalModules;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Integer getTotalModules() {
        return totalModules;
    }

    public void setTotalModules(Integer totalModules) {
        this.totalModules = totalModules;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private String instructor;
        private Integer totalModules;
        private String createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder instructor(String instructor) {
            this.instructor = instructor;
            return this;
        }

        public Builder totalModules(Integer totalModules) {
            this.totalModules = totalModules;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CourseDTO build() {
            return new CourseDTO(id, title, description, instructor, totalModules, createdAt);
        }
    }
}
