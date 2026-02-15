package com.elearning.enrollment.controller;

import com.elearning.enrollment.dto.EnrollmentDTO;
import com.elearning.enrollment.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin("*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<EnrollmentDTO> enrollStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enrollStudent(studentId, courseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getStudentEnrollments(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getCourseEnrollments(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCourseEnrollments(courseId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<EnrollmentDTO> completeEnrollment(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.completeEnrollment(id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<EnrollmentDTO> cancelEnrollment(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.cancelEnrollment(id));
    }
}
