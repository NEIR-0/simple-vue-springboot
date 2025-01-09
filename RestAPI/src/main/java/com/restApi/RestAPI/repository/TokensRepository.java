package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.token.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokensRepository extends JpaRepository<Tokens, Long> {
    @Query("SELECT t FROM Tokens t ORDER BY t.createdAt DESC")
    List<Tokens> findAllTokensOrderByCreatedAtDesc();
}
