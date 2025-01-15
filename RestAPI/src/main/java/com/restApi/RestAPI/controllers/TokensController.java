package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.tokenDTO.TokenDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.dto.tokenDTO.UpdateTokenDTO;
import com.restApi.RestAPI.model.token.Tokens;
import com.restApi.RestAPI.services.TokensService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/token")
public class TokensController {
    @Autowired
    private TokensService tokensService;

    @GetMapping
    public Map<String, Object> getAllTokens(
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "profit", required = false) String profit,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            HttpServletRequest request
    ){
        String userRole = (String) request.getAttribute("userRole");
        Map<String, Object> tokensData = tokensService.getAllTokens(userRole, sort, price, profit, page, size);

        // Mapping data tokens ke DTO
        List<TokenDTO> tokenDTOList = ((List<Tokens>) tokensData.get("tokens")).stream().map(token ->
                new TokenDTO(
                        token.getId(),
                        token.getName(),
                        token.getTokenPrice(),
                        token.getSymbol(),
                        token.getProfitPersen(),
                        token.getStatus(),
                        token.getTotalSupply(),
                        token.getAddressToken(),
                        token.getTotalBurn(),
                        token.getAlreadyBurn()
                )
        ).toList();

        // Kembalikan response dengan data paginasi
        Map<String, Object> response = new HashMap<>();
        response.put("tokens", tokenDTOList);
        response.put("totalPages", tokensData.get("totalPages"));
        response.put("totalItems", tokensData.get("totalItems"));
        return response;
    }

    @GetMapping("/{tokenId}")
    public TokenDTO getAllTokens(
            @PathVariable Long tokenId
    ){
        Optional<Tokens> token = tokensService.findById(tokenId);

        // Return response dalam bentuk TokenDTO
        return new TokenDTO(
                token.get().getId(),
                token.get().getName(),
                token.get().getTokenPrice(),
                token.get().getSymbol(),
                token.get().getProfitPersen(),
                token.get().getStatus(),
                token.get().getTotalSupply(),
                token.get().getAddressToken(),
                token.get().getTotalBurn(),
                token.get().getAlreadyBurn()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createTokens(
            @RequestBody Tokens inputUser,
            HttpServletRequest request
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        Long userId = (Long) request.getAttribute("userId");
        ResponseDTOOutput response =  tokensService.createTokens(inputUser, userId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create transactions");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }

    @PutMapping("/update/{tokenId}")
    public ResponseEntity<ResponseDTOOutput> UpdateTokens(
            @RequestBody UpdateTokenDTO updateTokenDTO,
            @PathVariable Long tokenId
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        ResponseDTOOutput response =  tokensService.updateTokens(updateTokenDTO, tokenId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create token");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }

    @PutMapping("/update-burn/{tokenId}")
    public ResponseEntity<ResponseDTOOutput> UpdateTokens(
            @PathVariable Long tokenId
    ) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();
        ResponseDTOOutput response =  tokensService.updateTokensBurn(tokenId);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        }else {
            responseStatus.setMsg("failed due create token");
            responseStatus.setStatus("failed");
            return ResponseEntity.badRequest().body(responseStatus);
        }
    }
}
