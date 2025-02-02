package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.token.InvestorTokens;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestorsTokensRepository extends JpaRepository<InvestorTokens, Long> {
}
