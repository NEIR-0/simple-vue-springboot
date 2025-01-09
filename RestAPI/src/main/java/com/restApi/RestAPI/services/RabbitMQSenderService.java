package com.restApi.RestAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.message.Messages;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.model.transaction.Transactions;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSenderService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Long sendMessageForTransactions(Transactions transaction) throws JsonProcessingException {
        System.out.println("Transaction message sent to RabbitMQ: " + transaction);
        String transactionJson = objectMapper.writeValueAsString(transaction);
        // convertSendAndReceive = buat ada balikannya
        String response = (String) amqpTemplate.convertSendAndReceive("myExchange", "transaction.routing.key", transactionJson);
        if (response != null) {
            Long transactionId = Long.parseLong(response);
            System.out.println("Response received from RabbitMQ: Transaction ID = " + transactionId);
            return transactionId;
        } else {
            System.out.println("No response received from RabbitMQ.");
            return null;
        }
    }

    public void sendMessageForRegisterUser(Users inputUser) throws JsonProcessingException {
        System.out.println("RegisterUser message sent to RabbitMQ: " + inputUser);
        String registerUserJson = objectMapper.writeValueAsString(inputUser);
        amqpTemplate.convertAndSend("myExchange", "user.routing.key", registerUserJson);
    }

    public String sendMessageForCreateMessage(Messages inputUser) throws JsonProcessingException {
        System.out.println("CreateMessage message sent to RabbitMQ: " + inputUser);
        String createMessageJson = objectMapper.writeValueAsString(inputUser);
        return (String) amqpTemplate.convertSendAndReceive("myExchange", "message.routing.key", createMessageJson);
    }

    public void sendMessageForTokens(Tokens inputUser) throws JsonProcessingException {
        System.out.println("CreateToken message sent to RabbitMQ: " + inputUser);
        String createMessageJson = objectMapper.writeValueAsString(inputUser);
        amqpTemplate.convertSendAndReceive("myExchange", "token.routing.key", createMessageJson);
    }
}
