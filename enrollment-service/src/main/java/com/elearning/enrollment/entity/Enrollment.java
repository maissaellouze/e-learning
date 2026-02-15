package com.elearning.enrollment.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate = LocalDateTime.now();

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    public Enrollment() {
    }

    public Enrollment(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = LocalDateTime.now();
        this.status = EnrollmentStatus.ACTIVE;
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

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    public static EnrollmentBuilder builder() {
        return new EnrollmentBuilder();
    }

    public static class EnrollmentBuilder {
        private Long studentId;
        private Long courseId;
        private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

        public EnrollmentBuilder studentId(Long studentId) {
            this.studentId = studentId;
            return this;
        }

        public EnrollmentBuilder courseId(Long courseId) {
            this.courseId = courseId;
            return this;
        }

        public EnrollmentBuilder status(EnrollmentStatus status) {
            this.status = status;
            return this;
        }

        public Enrollment build() {
            Enrollment enrollment = new Enrollment(studentId, courseId);
            enrollment.status = status;
            return enrollment;
        }
    }
}
