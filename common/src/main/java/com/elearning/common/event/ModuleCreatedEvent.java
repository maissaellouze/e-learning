package com.elearning.common.event;

import java.io.Serializable;

public class ModuleCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long courseId;
    private String courseName;
    private String moduleName;
    private String moduleDescription;
    private String createdAt;

    public ModuleCreatedEvent() {
    }

    public ModuleCreatedEvent(Long courseId, String courseName, String moduleName, String moduleDescription, String createdAt) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.moduleName = moduleName;
        this.moduleDescription = moduleDescription;
        this.createdAt = createdAt;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
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
        private Long courseId;
        private String courseName;
        private String moduleName;
        private String moduleDescription;
        private String createdAt;

        public Builder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public Builder courseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public Builder moduleName(String moduleName) {
            this.moduleName = moduleName;
            return this;
        }

        public Builder moduleDescription(String moduleDescription) {
            this.moduleDescription = moduleDescription;
            return this;
        }

        public Builder createdAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ModuleCreatedEvent build() {
            return new ModuleCreatedEvent(courseId, courseName, moduleName, moduleDescription, createdAt);
        }
    }
}
