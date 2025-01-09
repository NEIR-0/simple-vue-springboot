package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.TokenDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.services.TokensService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/token")
public class TokensController {
    @Autowired
    private TokensService tokensService;

    @GetMapping
    public List<TokenDTO> getAllTokens(){
        List<Tokens> tokensList =  tokensService.getAllTokens();
        return tokensList.stream().map(token ->
                new TokenDTO(
                        token.getName(),
                        token.getTokenPrice(),
                        token.getSymbol(),
                        token.getProfitPersen(),
                        token.getStatus(),
                        token.getTotalSupply(),
                        token.getAddressToken()
                )
        ).collect(Collectors.toList());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createTransactions(
            @RequestBody Tokens inputUser,
            HttpServletRequest request
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("userId: " + userId + ">>>>>>>>>>>");
        ResponseDTOOutput response =  tokensService.createTransactions(inputUser, userId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create transactions");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }
}
