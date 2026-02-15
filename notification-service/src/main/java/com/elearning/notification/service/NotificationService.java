package com.elearning.notification.service;

import com.elearning.common.event.ModuleCreatedEvent;
import com.elearning.notification.dto.NotificationDTO;
import com.elearning.notification.entity.Notification;
import com.elearning.notification.entity.NotificationType;
import com.elearning.notification.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Handle module created events from RabbitMQ
    @RabbitListener(queues = "notification-queue")
    public void handleModuleCreatedEvent(ModuleCreatedEvent event) {
        System.out.println("Received module created event for course: " + event.getCourseName());

        // In a real scenario, we would fetch all students enrolled in this course
        // For demonstration, we'll create a notification with a placeholder student ID
        String message = String.format("New module '%s' has been added to course '%s'",
                event.getModuleName(), event.getCourseName());

        // This would typically be called for each enrolled student
        createNotificationForStudents(event.getCourseId(), message, event, NotificationType.MODULE_CREATED);
    }

    public NotificationDTO createNotification(Long studentId, String message, String details, NotificationType type) {
        Notification notification = Notification.builder()
                .studentId(studentId)
                .message(message)
                .details(details)
                .type(type)
                .isRead(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        System.out.println("Notification created for student: " + studentId);

        return convertToDTO(saved);
    }

    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return convertToDTO(notification);
    }

    public List<NotificationDTO> getStudentNotifications(Long studentId) {
        return notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<NotificationDTO> getUnreadNotifications(Long studentId) {
        return notificationRepository.findByStudentIdAndIsReadFalseOrderByCreatedAtDesc(studentId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public NotificationDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);
        Notification updated = notificationRepository.save(notification);

        return convertToDTO(updated);
    }

    // Reactive operations
    public Mono<NotificationDTO> getNotificationMono(Long id) {
        return Mono.fromCallable(() -> notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found")))
                .map(this::convertToDTO);
    }

    public Flux<NotificationDTO> getStudentNotificationsFlux(Long studentId) {
        return Flux.fromIterable(notificationRepository.findByStudentIdOrderByCreatedAtDesc(studentId))
                .map(this::convertToDTO);
    }

    private void createNotificationForStudents(Long courseId, String message, ModuleCreatedEvent event, NotificationType type) {
        // In production, fetch enrolled students for this course from Enrollment Service
        // For now, we'll create a sample notification
        Notification notification = Notification.builder()
                .studentId(1L) // Placeholder
                .message(message)
                .details("Course: " + event.getCourseName() + ", Module: " + event.getModuleName())
                .type(type)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
        System.out.println("Notification created for course " + courseId + ": " + message);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .studentId(notification.getStudentId())
                .message(notification.getMessage())
                .details(notification.getDetails())
                .type(notification.getType().toString())
                .createdAt(notification.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME))
                .isRead(notification.getIsRead())
                .status(notification.getIsRead() ? "READ" : "SENT")
                .build();
    }
}
