package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.notification.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    // Menggunakan JPQL
    @Query("SELECT n FROM Notifications n WHERE n.userId.id = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notifications> getAllUnreadNotifications(@Param("userId") Long userId);

    @Query("SELECT n FROM Notifications n WHERE n.token.id = :tokenId ORDER BY n.createdAt DESC LIMIT 1")
    Optional<Notifications> findLatestByTokenId(@Param("tokenId") Long tokenId);
}
