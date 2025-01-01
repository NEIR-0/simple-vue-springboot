package com.restApi.RestAPI.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.message.Messages;
import com.restApi.RestAPI.model.transaction.Transactions;
import com.restApi.RestAPI.repository.MessagesRepository;
import com.restApi.RestAPI.repository.TransactionsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReceiverService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MessagesRepository messagesRepository;

    @RabbitListener(queues = "transactionQueue")
    public String receiveTransaction(String transactionJson) {
        try {
            // Konversi JSON ke objek Transactions
            Transactions transaction = objectMapper.readValue(transactionJson, Transactions.class);
            System.out.println("Processing transaction in the background: " + transaction);
            Transactions savedTransaction = processTransaction(transaction);

            // Kembalikan ID transaksi sebagai respons
            return String.valueOf(savedTransaction.getId());
        } catch (Exception e) {
            System.out.println("Error processing transaction: " + e.getMessage());
            return null; // Kembalikan null jika terjadi error
        }
    }
    private Transactions processTransaction(Transactions transaction) {
        Transactions savedTransaction = transactionsRepository.save(transaction);
        System.out.println("Transaction processed id: " + savedTransaction.getId());
        return savedTransaction;
    }

    @RabbitListener(queues = "userQueue")
    public void receiveRegisterUser(String registerUserJson) {
        try {
            // Konversi JSON ke objek Transactions
            Users registerUser = objectMapper.readValue(registerUserJson, Users.class);
            System.out.println("Processing register user in the background: " + registerUser);
            processRegisterUser(registerUser);
        } catch (Exception e) {
            System.out.println("Error processing register user: " + e.getMessage());
        }
    }
    private void processRegisterUser(Users inputUser) {
        String savedTransaction = userService.createUser(inputUser);
        System.out.println(savedTransaction);
    }

    @RabbitListener(queues = "messageQueue")
    public String receiveMessageCreate(String registerUserJson) {
        try {
            // Konversi JSON ke objek Transactions
            Messages messageCreate = objectMapper.readValue(registerUserJson, Messages.class);
            System.out.println("Processing create Message user in the background: " + messageCreate);
            processMessageCreate(messageCreate);
            return "success";
        } catch (Exception e) {
            System.out.println("Error processing register user: " + e.getMessage());
            return "failed";
        }
    }
    private void processMessageCreate(Messages inputUser) {
        messagesRepository.save(inputUser);
    }
}
