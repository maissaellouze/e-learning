package com.elearning.notification.controller;

import com.elearning.notification.dto.NotificationDTO;
import com.elearning.notification.entity.NotificationType;
import com.elearning.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(
            @RequestParam Long studentId,
            @RequestParam String message,
            @RequestParam(required = false) String details,
            @RequestParam String type) {
        NotificationType notificationType = NotificationType.valueOf(type.toUpperCase());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createNotification(studentId, message, details, notificationType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<NotificationDTO>> getStudentNotifications(@PathVariable Long studentId) {
        return ResponseEntity.ok(notificationService.getStudentNotifications(studentId));
    }

    @GetMapping("/student/{studentId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable Long studentId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(studentId));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // Reactive endpoints
    @GetMapping("/reactive/{id}")
    public Mono<ResponseEntity<NotificationDTO>> getNotificationByIdReactive(@PathVariable Long id) {
        return notificationService.getNotificationMono(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/reactive/student/{studentId}")
    public Flux<NotificationDTO> getStudentNotificationsReactive(@PathVariable Long studentId) {
        return notificationService.getStudentNotificationsFlux(studentId);
    }
}
