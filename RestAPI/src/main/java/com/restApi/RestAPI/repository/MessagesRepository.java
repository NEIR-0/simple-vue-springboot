package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.message.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessagesRepository extends JpaRepository<Messages, Long> {
    @Query("SELECT DISTINCT m FROM Messages m WHERE m.senderId.id = :userId OR m.receiverId.id = :userId")
    List<Messages> findAllMessagesByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT m FROM Messages m WHERE (m.senderId.id = :adminId AND m.receiverId.id = :userId) OR (m.senderId.id = :userId AND m.receiverId.id = :adminId)")
    List<Messages> findAllMessagesByUserIdAndAdminId(@Param("userId") Long userId, @Param("adminId") Long adminId);
}
