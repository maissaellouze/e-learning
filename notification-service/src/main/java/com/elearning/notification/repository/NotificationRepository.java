package com.elearning.notification.repository;

import com.elearning.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<Notification> findByStudentIdAndIsReadFalseOrderByCreatedAtDesc(Long studentId);
}
