package com.elearning.enrollment.dto;

public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String enrollmentDate;
    private String completionDate;
    private String status;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Long id, Long studentId, Long courseId, String enrollmentDate, String completionDate, String status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.completionDate = completionDate;
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static EnrollmentDTOBuilder builder() {
        return new EnrollmentDTOBuilder();
    }

    public static class EnrollmentDTOBuilder {
        private Long id;
        private Long studentId;
        private Long courseId;
        private String enrollmentDate;
        private String completionDate;
        private String status;

        public EnrollmentDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EnrollmentDTOBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public EnrollmentDTOBuilder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public EnrollmentDTOBuilder enrollmentDate(String enrollmentDate) {
            this.enrollmentDate = enrollmentDate;
            return this;
        }

        public EnrollmentDTOBuilder completionDate(String completionDate) {
            this.completionDate = completionDate;
            return this;
        }

        public EnrollmentDTOBuilder status(String status) {
            this.status = status;
            return this;
        }

        public EnrollmentDTO build() {
            return new EnrollmentDTO(id, studentId, courseId, enrollmentDate, completionDate, status);
        }
    }
}
