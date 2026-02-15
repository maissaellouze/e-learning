package com.elearning.course.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Long courseId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Module() {
    }

    public Module(String name, String description, Long courseId) {
        this.name = name;
        this.description = description;
        this.courseId = courseId;
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static ModuleBuilder builder() {
        return new ModuleBuilder();
    }

    public static class ModuleBuilder {
        private String name;
        private String description;
        private Long courseId;

        public ModuleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ModuleBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ModuleBuilder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public Module build() {
            return new Module(name, description, courseId);
        }
    }
}
