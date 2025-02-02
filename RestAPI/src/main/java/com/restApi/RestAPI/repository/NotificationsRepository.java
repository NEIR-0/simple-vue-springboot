package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.dto.NotificationDTO;
import com.restApi.RestAPI.model.notification.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    // Menggunakan JPQL
    @Query("SELECT new com.restApi.RestAPI.dto.NotificationDTO(n.id, n.status, t.name, t.status, n.createdAt) " +
            "FROM Notifications n " +
            "JOIN n.token t " +
            "WHERE n.userId.id = :userId AND n.isRead = false " +
            "ORDER BY n.createdAt DESC")
    List<NotificationDTO> getAllUnreadNotifications(@Param("userId") Long userId);

    @Query("SELECT n FROM Notifications n WHERE n.token.id = :tokenId ORDER BY n.createdAt DESC LIMIT 1")
    Optional<Notifications> findLatestByTokenId(@Param("tokenId") Long tokenId);
}
