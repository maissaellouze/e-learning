package com.elearning.course.dto;

public class ModuleDTO {
    private Long id;
    private String name;
    private String description;
    private Long courseId;
    private String createdAt;

    public ModuleDTO() {
    }

    public ModuleDTO(Long id, String name, String description, Long courseId, String createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseId = courseId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static ModuleDTOBuilder builder() {
        return new ModuleDTOBuilder();
    }

    public static class ModuleDTOBuilder {
        private Long id;
        private String name;
        private String description;
        private Long courseId;
        private String createdAt;

        public ModuleDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ModuleDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ModuleDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ModuleDTOBuilder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public ModuleDTOBuilder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ModuleDTO build() {
            return new ModuleDTO(id, name, description, courseId, createdAt);
        }
    }
}
