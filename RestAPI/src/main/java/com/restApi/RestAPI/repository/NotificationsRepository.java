package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.notification.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    // Menggunakan JPQL
    @Query("SELECT n FROM Notifications n WHERE n.userId.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notifications> getAllUnreadNotifications(@Param("userId") Long userId);
}
