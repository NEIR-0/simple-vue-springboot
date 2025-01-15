package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.token.Tokens;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokensRepository extends JpaRepository<Tokens, Long> {
    @Query("SELECT t FROM Tokens t ORDER BY t.createdAt DESC")
    List<Tokens> findAllTokensOrderByCreatedAtDesc();

    @Query("SELECT t FROM Tokens t")
    Page<Tokens> findAllTokens(Pageable pageable);

    @Query("SELECT t FROM Tokens t WHERE t.totalSupply > 0 AND t.status = 'ongoing' ORDER BY t.createdAt DESC")
    List<Tokens> findAllOngoingTokensWithSupplyOrderByCreatedAtDesc();

    @Query("SELECT t FROM Tokens t WHERE t.totalSupply > 0 AND t.status = 'ongoing'")
    Page<Tokens> findAllOngoingTokensWithSupply(Pageable pageable);
}
