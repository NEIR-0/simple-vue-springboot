package com.restApi.RestAPI.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.dto.tokenDTO.BuyTokenDTO;
import com.restApi.RestAPI.dto.tokenDTO.InvestorTokensDTO;
import com.restApi.RestAPI.dto.tokenDTO.UpdateTokenDTO;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.notification.Notifications;
import com.restApi.RestAPI.model.token.InvestorTokens;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.repository.InvestorsTokensRepository;
import com.restApi.RestAPI.repository.NotificationsRepository;
import com.restApi.RestAPI.repository.TokensRepository;
import com.restApi.RestAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class TokensService {
    @Autowired
    private RabbitMQSenderService rabbitMQSenderService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokensRepository tokensRepository;

    @Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    InvestorsTokensRepository investorsTokensRepository;

    public Map<String, Object> getAllTokens(String userRole, String sort, String price, String profit, Integer page, Integer size) {
        String sortField = "createdAt";
        Sort.Direction direction = Sort.Direction.DESC;

        if (sort != null) {
            direction = "asc".equalsIgnoreCase(sort) ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortField = "createdAt";
        }
        else if (price != null) {
            direction = "asc".equalsIgnoreCase(price) ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortField = "tokenPrice";
        }
        else if (profit != null) {
            direction = "asc".equalsIgnoreCase(profit) ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortField = "profitPersen";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        Page<Tokens> tokensPage;
        List<Tokens> totalTokens;

        if (Objects.equals(userRole, "admin")) {
            System.out.println("admin >>>??");
            tokensPage = tokensRepository.findAllTokens(pageable);
            totalTokens = tokensRepository.findAllTokensOrderByCreatedAtDesc();
        } else {
            System.out.println("user >>>??");
            tokensPage = tokensRepository.findAllOngoingTokensWithSupply(pageable);
            totalTokens = tokensRepository.findAllOngoingTokensWithSupplyOrderByCreatedAtDesc();
        }

        // Membuat response map
        Map<String, Object> response = new HashMap<>();
        response.put("tokens", tokensPage.getContent());
        response.put("totalPages", tokensPage.getTotalPages());
        response.put("totalItems", totalTokens.size());

        return response;
    }

    public Optional<Tokens> findById(Long tokenId) {
        return tokensRepository.findById(tokenId);
    }

    public ResponseDTOOutput createTokens(Tokens inputUser, Long userId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        // Membuat transaksi baru di database
        Tokens token = new Tokens();
        token.setSymbol(inputUser.getSymbol());
        token.setName(inputUser.getName());
        token.setTokenPrice(inputUser.getTokenPrice());
        token.setHash(inputUser.getHash());
        token.setAddressCreator(inputUser.getAddressCreator());
        token.setAddressToken(inputUser.getAddressToken());
        token.setTotalSupply(inputUser.getTotalSupply());
        token.setTotalBurn(inputUser.getTotalBurn());
        token.setAlreadyBurn(inputUser.getAlreadyBurn());
        token.setWithdraw(false);
        token.setApprove(inputUser.isApprove());
        token.setStatus(inputUser.getStatus());
        token.setProfitPersen(inputUser.getProfitPersen());
        token.setAddressToken(inputUser.getAddressToken());

        //  notifications
        Notifications notif = new Notifications();
        notif.setIsRead(false);
        notif.setTxHash(inputUser.getHash());
        notif.setStatus("create_token");

        System.out.println("userId: " + userId + ">>>>>>>>>>>");
        Optional<Users> user = userRepository.findById(userId);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getId());
            token.setCreatorId(user.orElse(null));
            notif.setUserId(user.orElse(null));
        } else {
            System.out.println("User not found!");
        }

        try {
            // Mengirim transaksi ke RabbitMQ
            Long tokenId = rabbitMQSenderService.sendMessageForTokens(token);
            System.out.println("token_id: " + tokenId + ">>>>>>>>>>>");

            Optional<Tokens> tokenData = tokensRepository.findById(tokenId);
            if (tokenData.isPresent()) {
                System.out.println("token found: " + tokenData.get().getId());
                notif.setToken(tokenData.orElse(null));
                rabbitMQSenderService.sendMessageForNotif(notif);
            } else {
                System.out.println("token not found!");
            }

            responseStatus.setMsg("userId-" + userId);
            responseStatus.setStatus("success");
        } catch (JsonProcessingException e) {
            System.out.println("Error RabbitMQ Transaction: " + e.getMessage());
            responseStatus.setMsg("Failed to process transaction.");
            responseStatus.setStatus("failed");
        }

        return responseStatus;
    }

    public ResponseDTOOutput investTokens(BuyTokenDTO inputUser, Long tokenId, Long userId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        // Membuat transaksi baru di database
        InvestorTokensDTO investors = new InvestorTokensDTO();
        investors.setAddressInvestor(inputUser.getAddressInvestor());
        investors.setHash(inputUser.getHash());
        investors.setHoldToken(inputUser.getHoldToken());
        investors.setHoldAfterBurn(inputUser.getHoldAfterBurn());
        investors.setProfitBurn(inputUser.getProfitBurn());

        //  notifications
        Notifications notif = new Notifications();
        notif.setIsRead(false);
        notif.setTxHash(inputUser.getHash());
        notif.setStatus("buy_token");

        System.out.println("userId: " + userId + ">>>>>>>>>>>");
        Optional<Users> user = userRepository.findById(userId);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getId());
            investors.setUsers(user.orElse(null));
            notif.setUserId(user.orElse(null));
        } else {
            System.out.println("User not found!");
        }

        try {
            // Mengirim transaksi ke RabbitMQ
            Optional<Tokens> tokenData = tokensRepository.findById(tokenId);
            if (tokenData.isPresent()) {
                System.out.println("token found: " + tokenData.get().getId());
                notif.setToken(tokenData.orElse(null));
                rabbitMQSenderService.sendMessageForNotif(notif);
                investors.setTokenId(tokenId);
                rabbitMQSenderService.sendMessageForInvestment(investors);
            } else {
                System.out.println("token not found!");
            }

            responseStatus.setMsg("userId-" + userId);
            responseStatus.setStatus("success");
        } catch (JsonProcessingException e) {
            System.out.println("Error RabbitMQ Transaction: " + e.getMessage());
            responseStatus.setMsg("Failed to process transaction.");
            responseStatus.setStatus("failed");
        }

        return responseStatus;
    }

    public ResponseDTOOutput updateTokens(UpdateTokenDTO inputUser, Long tokenId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        Tokens existingToken = tokensRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("token not found"));

        existingToken.setProfitPersen(inputUser.getProfitPersen());
        existingToken.setStatus(inputUser.getStatus());
        existingToken.setTotalBurn(inputUser.getTotalBurn());
        existingToken.setTotalSupply(inputUser.getTotalSupply());
        existingToken.setPayPerBurn(inputUser.getPayPerBurn());
        existingToken.setBurnTempo(inputUser.getBurnTempo());
        double totalSupply = inputUser.getTotalSupply();
        int totalBurn = inputUser.getTotalBurn();
        BigDecimal amountPerBurn = BigDecimal.valueOf(totalSupply)
                .divide(BigDecimal.valueOf(totalBurn), 0, RoundingMode.DOWN);
        existingToken.setAmountPerBurning(amountPerBurn.doubleValue());

        Calendar calendar = Calendar.getInstance();
        if (inputUser.getBurnTempo().startsWith("Monthly")) {
            calendar.add(Calendar.MONTH, 1);
        } else if (inputUser.getBurnTempo().startsWith("Quarterly")) {
            calendar.add(Calendar.MONTH, 3);
        } else if (inputUser.getBurnTempo().startsWith("Half")) {
            calendar.add(Calendar.MONTH, 6);
        } else if (inputUser.getBurnTempo().startsWith("Yearly")) {
            calendar.add(Calendar.YEAR, 1);
        } else {
            System.out.println("Invalid selection BURN DATE");
        }
        existingToken.setBurnDate(calendar.getTime());

        Optional<Notifications> latestNotif = notificationsRepository.findLatestByTokenId(existingToken.getId());
        latestNotif.ifPresent(notification -> {
            Notifications newNotif = new Notifications();
            newNotif.setIsRead(false);
            newNotif.setTxHash(notification.getTxHash());
            newNotif.setStatus("minting_token");

            Optional<Users> user = userRepository.findById(notification.getUserId().getId());
            if (user.isPresent()) {
                System.out.println("User found: " + user.get().getId());
                newNotif.setUserId(user.orElse(null));
                responseStatus.setMsg("userId-" + user.get().getId());
            } else {
                System.out.println("User not found!");
            }

            Optional<Tokens> tokenData = tokensRepository.findById(notification.getToken().getId());
            if (tokenData.isPresent()) {
                System.out.println("token found: " + tokenData.get().getId());
                newNotif.setToken(tokenData.orElse(null));
                try {
                    rabbitMQSenderService.sendMessageForNotif(newNotif);
                    tokensRepository.save(existingToken);
                } catch (JsonProcessingException e) {
                    System.out.println("Error create notifications minting");
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("token not found!");
            }
        });
        responseStatus.setStatus("success");
        return responseStatus;
    }

    public ResponseDTOOutput updateTokensBurn(Long tokenId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        Tokens existingToken = tokensRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("token not found"));

        if(existingToken.getTotalBurn() - existingToken.getAlreadyBurn()  != 0 ){
            existingToken.setAlreadyBurn(existingToken.getAlreadyBurn() + 1);
        }

        if(existingToken.getTotalBurn() == existingToken.getAlreadyBurn() ){
            existingToken.setStatus("close");
        }

        // Ambil burn date dan konversi ke Calendar
        Calendar current = Calendar.getInstance();
        current.setTime(existingToken.getBurnDate());
        if (existingToken.getBurnTempo().startsWith("Monthly")) {
            current.add(Calendar.MONTH, 1);  // Tambahkan 1 bulan
        } else if (existingToken.getBurnTempo().startsWith("Quarterly")) {
            current.add(Calendar.MONTH, 3);  // Tambahkan 3 bulan
        } else if (existingToken.getBurnTempo().startsWith("Half")) {
            current.add(Calendar.MONTH, 6);  // Tambahkan 6 bulan
        } else if (existingToken.getBurnTempo().startsWith("Yearly")) {
            current.add(Calendar.YEAR, 1);   // Tambahkan 1 tahun
        } else {
            System.out.println("Invalid selection for BURN DATE");
        }
        existingToken.setBurnDate(current.getTime());

        // Update token in repository
        tokensRepository.save(existingToken);
        System.out.println("Token updated: " + existingToken.getId());

        // Get all investors of the token
        List<InvestorTokens> investors = investorsTokensRepository.findByToken(existingToken);
        System.out.println("Found " + investors.size() + " investors for token ID: " + tokenId);

        // Loop through each investor and create notification
        for (InvestorTokens investor : investors) {
            Notifications newNotif = new Notifications();
            newNotif.setIsRead(false);
            newNotif.setTxHash(investor.getHash());
            newNotif.setStatus("burn_token-" + existingToken.getAlreadyBurn());

            Optional<Users> user = userRepository.findById(investor.getUsers().getId());
            if (user.isPresent()) {
                System.out.println("User found: " + user.get().getId());
                newNotif.setUserId(user.get());
            } else {
                System.out.println("User not found for investor ID: " + investor.getId());
                continue;  // Skip this iteration if user not found
            }

            Optional<Tokens> tokenData = tokensRepository.findById(existingToken.getId());
            if (tokenData.isPresent()) {
                newNotif.setToken(tokenData.get());
                try {
                    rabbitMQSenderService.sendMessageForNotif(newNotif);
                    System.out.println("Notification sent for investor ID: " + investor.getId());
                } catch (JsonProcessingException e) {
                    System.out.println("Error creating notification for investor ID: " + investor.getId());
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Token not found for notification");
            }
        }

        responseStatus.setStatus("success");
        return responseStatus;
    }

    public ResponseDTOOutput updatewithdrawTokens(BuyTokenDTO inputUser, Long tokenId, Long userId) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        Tokens existingToken = tokensRepository.findById(tokenId)
                .orElseThrow(() -> new RuntimeException("token not found"));

        existingToken.setWithdraw(true);

        Notifications newNotif = new Notifications();
        newNotif.setIsRead(false);
        newNotif.setTxHash(inputUser.getHash());
        newNotif.setStatus("withdraw_token");

        Optional<Users> user = userRepository.findById(userId);
        if (user.isPresent()) {
            System.out.println("User found: " + user.get().getId());
            newNotif.setUserId(user.orElse(null));
            responseStatus.setMsg("userId-" + user.get().getId());
        } else {
            System.out.println("User not found!");
        }

        Optional<Tokens> tokenData = tokensRepository.findById(existingToken.getId());
        if (tokenData.isPresent()) {
            System.out.println("token found: " + tokenData.get().getId());
            newNotif.setToken(tokenData.orElse(null));
            try {
                rabbitMQSenderService.sendMessageForNotif(newNotif);
                tokensRepository.save(existingToken);
            } catch (JsonProcessingException e) {
                System.out.println("Error create notifications withdraw");
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("token not found!");
        }
        responseStatus.setStatus("success");
        return responseStatus;
    }
}
