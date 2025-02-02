package com.restApi.RestAPI.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restApi.RestAPI.dto.tokenDTO.InvestorTokensDTO;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.message.Messages;
import com.restApi.RestAPI.model.notification.Notifications;
import com.restApi.RestAPI.model.token.InvestorTokens;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.model.transaction.Transactions;
import com.restApi.RestAPI.repository.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokensRepository tokensRepository;

    @Autowired
    NotificationsService notificationsService;

    @Autowired
    InvestorsTokensRepository investorsTokensRepository;

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
            System.out.println("Error processing register Message: " + e.getMessage());
            return "failed";
        }
    }
    private void processMessageCreate(Messages inputUser) {
        messagesRepository.save(inputUser);
    }


    @RabbitListener(queues = "tokenQueue")
    public String receiveTokenCreate(String registerUserJson) {
        try {
            // Konversi JSON ke objek Transactions
            Tokens tokenCreate = objectMapper.readValue(registerUserJson, Tokens.class);
            System.out.println("Processing create token user in the background: " + tokenCreate);
            Tokens savedTokens = processTokenCreate(tokenCreate);
            return String.valueOf(savedTokens.getId());
        } catch (Exception e) {
            System.out.println("Error processing register token: " + e.getMessage());
            return null; // Kembalikan null jika terjadi error
        }
    }
    private Tokens processTokenCreate(Tokens inputUser) {
        Tokens savedTokens = tokensRepository.save(inputUser);
        System.out.println("Transaction processed id: " + savedTokens.getId());
        return savedTokens;
    }

    @RabbitListener(queues = "notifQueue")
    public void receiveNotificationsUser(String notificationsJson) {
        try {
            // Konversi JSON ke objek Transactions
            Notifications notification = objectMapper.readValue(notificationsJson, Notifications.class);
            System.out.println("Processing notification user in the background: " + notification);
            processNotificationsUser(notification);
        } catch (Exception e) {
            System.out.println("Error processing notification user: " + e.getMessage());
        }
    }
    private void processNotificationsUser(Notifications inputUser) {
        String savedTransaction = notificationsService.createNotifications(inputUser);
        System.out.println(savedTransaction);
    }

    @RabbitListener(queues = "investQueue")
    public void receiveInvestmentsUser(String investmentJson) {
        try {
            // Konversi JSON ke objek Transactions
            InvestorTokensDTO investment = objectMapper.readValue(investmentJson, InvestorTokensDTO.class);
            System.out.println("Processing Investment user in the background: " + investment);
            processInvestmentUser(investment);
        } catch (Exception e) {
            System.out.println("Error processing register Investment: " + e.getMessage());
        }
    }
    private void processInvestmentUser(InvestorTokensDTO inputUser) {
        InvestorTokens investors = new InvestorTokens();
        investors.setAddressInvestor(inputUser.getAddressInvestor());
        investors.setHash(inputUser.getHash());
        investors.setHoldToken(inputUser.getHoldToken());
        investors.setHoldAfterBurn(inputUser.getHoldAfterBurn());
        investors.setProfitBurn(inputUser.getProfitBurn());
        investors.setUsers(inputUser.getUsers());

        Optional<Tokens> tokenData = tokensRepository.findById(inputUser.getTokenId());
        if (tokenData.isPresent()) {
            System.out.println("token found on rabitmq: " + tokenData.get().getId());
            investors.setToken(tokenData.orElse(null));
            investorsTokensRepository.save(investors);
            System.out.println("Berhasil create investment");
        } else {
            System.out.println("token not found!");
        }
    }
}
