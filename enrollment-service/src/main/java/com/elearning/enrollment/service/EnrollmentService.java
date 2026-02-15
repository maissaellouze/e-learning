package com.elearning.enrollment.service;

import com.elearning.enrollment.dto.EnrollmentDTO;
import com.elearning.enrollment.entity.Enrollment;
import com.elearning.enrollment.entity.EnrollmentStatus;
import com.elearning.enrollment.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentDTO enrollStudent(Long studentId, Long courseId) {
        // Check if already enrolled
        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .courseId(courseId)
                .status(EnrollmentStatus.ACTIVE)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        System.out.println("Student " + studentId + " enrolled in course " + courseId);

        return convertToDTO(saved);
    }

    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        return convertToDTO(enrollment);
    }

    public List<EnrollmentDTO> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<EnrollmentDTO> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public EnrollmentDTO completeEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollment.setCompletionDate(LocalDateTime.now());

        Enrollment updated = enrollmentRepository.save(enrollment);
        System.out.println("Enrollment " + enrollmentId + " marked as completed");

        return convertToDTO(updated);
    }

    public EnrollmentDTO cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(EnrollmentStatus.CANCELED);

        Enrollment updated = enrollmentRepository.save(enrollment);
        System.out.println("Enrollment " + enrollmentId + " canceled");

        return convertToDTO(updated);
    }

    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        return EnrollmentDTO.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .courseId(enrollment.getCourseId())
                .enrollmentDate(enrollment.getEnrollmentDate().format(DateTimeFormatter.ISO_DATE_TIME))
                .completionDate(enrollment.getCompletionDate() != null ? 
                        enrollment.getCompletionDate().format(DateTimeFormatter.ISO_DATE_TIME) : null)
                .status(enrollment.getStatus().toString())
                .build();
    }
}
