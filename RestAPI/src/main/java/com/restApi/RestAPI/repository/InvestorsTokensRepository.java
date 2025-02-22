package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.token.InvestorTokens;
import com.restApi.RestAPI.model.token.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestorsTokensRepository extends JpaRepository<InvestorTokens, Long> {
    List<InvestorTokens> findByToken(Tokens token);
}

