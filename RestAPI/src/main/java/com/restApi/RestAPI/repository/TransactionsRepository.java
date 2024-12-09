package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.transaction.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
}
