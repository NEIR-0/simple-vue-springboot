package com.restApi.RestAPI.services;

import com.restApi.RestAPI.repository.TransactionsRepository;
import com.restApi.RestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionsService {
    @Autowired
    private TransactionsRepository transactionsRepository;

}
