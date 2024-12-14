package com.restApi.RestAPI.services;

import com.restApi.RestAPI.config.JwtUtil;
import com.restApi.RestAPI.dto.inputUserDTO.TransactionDTOUserInput;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.auth.Users;
import com.restApi.RestAPI.model.product.Products;
import com.restApi.RestAPI.model.transaction.Transactions;
import com.restApi.RestAPI.repository.ProductsRepository;
import com.restApi.RestAPI.repository.TransactionsRepository;
import com.restApi.RestAPI.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductsService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    ProductsRepository productsRepository;

    public List<Products> getAllProducts(){
        return productsRepository.findAll();
    }

    public Products getProductsById(Long productId) {
        if (productId != null) {
            return productsRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("product not found"));
        }
        return null;
    }

    public ResponseDTOOutput createProduct(Products inputUser) {
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        if (inputUser != null) {
            productsRepository.save(inputUser);

        responseStatus.setMsg("products created successfully");
        responseStatus.setStatus("success");
        return responseStatus;
        }else{
            responseStatus.setMsg("failed due create products");
            responseStatus.setStatus("failed");
            return responseStatus;
        }
    }

    public ResponseDTOOutput updateProduct(Products inputProduct, Long productId) {
        System.out.println("mask sini?");
        System.out.println(productId + "!?@?#!@?#");
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        if (productId != null) {
            System.out.println("mask sini >>>>");
            Products existingProduct = productsRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("product not found"));

            // Perbarui field yang tersedia dari inputProduct (dinamis)
            if (inputProduct.getTitle() != null && !inputProduct.getTitle().isEmpty()) {
                existingProduct.setTitle(inputProduct.getTitle());
            }
            if (inputProduct.getDescription() != null && !inputProduct.getDescription().isEmpty()) {
                existingProduct.setDescription(inputProduct.getDescription());
            }
            if (inputProduct.getPrice() != null) {
                existingProduct.setPrice(inputProduct.getPrice());
            }
            if (inputProduct.getImage() != null && !inputProduct.getImage().isEmpty()) {
                existingProduct.setImage(inputProduct.getImage());
            }

            // Simpan perubahan ke database
            productsRepository.save(existingProduct);

            responseStatus.setMsg("products updated successfully");
            responseStatus.setStatus("success");
        }else{
            responseStatus.setMsg("failed due create products");
            responseStatus.setStatus("failed");
        }
        return responseStatus;
    }

    public ResponseDTOOutput deleteById(Long productId){
        ResponseDTOOutput responseStatus = new ResponseDTOOutput();

        if (productId != null) {
            productsRepository.deleteById(productId);

            responseStatus.setMsg("products deleted successfully");
            responseStatus.setStatus("success");
            return responseStatus;
        }else{
            responseStatus.setMsg("failed due create products");
            responseStatus.setStatus("failed");
            return responseStatus;
        }
    }

    public String createTransactions(TransactionDTOUserInput inputUser) {
        System.out.println(inputUser.getUserId() + "??????"); // 1
        System.out.println(inputUser.getPrice() + "??????");

        Users user = userRepository.findById(inputUser.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Users receiver = null;
        if (inputUser.getReceiverId() != null) {
            receiver = userRepository.findById(inputUser.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
        }

        Transactions transaction = new Transactions();
        transaction.setUser(user);
        transaction.setDes(inputUser.getDes());
        transaction.setPrice(inputUser.getPrice());
        transaction.setHash(inputUser.getHash());
        transaction.setStatus(inputUser.getStatus());

        // Simpan ke database
        transactionsRepository.save(transaction);

        return "Transaction created successfully!";
    }
}
