package com.restApi.RestAPI.controllers;

import com.restApi.RestAPI.dto.ProductsDTO;
import com.restApi.RestAPI.dto.outputDTO.ResponseDTOOutput;
import com.restApi.RestAPI.model.product.Products;
import com.restApi.RestAPI.services.ProductsService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProeductsController {

    @Autowired
    ProductsService productsService;

    @GetMapping
    public ProductsDTO getAllProducts(
        @RequestParam(value = "search", required = false) String search,
        @RequestParam(value = "duration", required = false) Integer duration,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        return productsService.getAllProducts(search, duration);
    }

    @GetMapping("/admin-site")
    public ProductsDTO getAllProductsAdminSite(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "duration", required = false) Integer duration,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ){
        return productsService.getAllProductsAdminSite(search, duration, page, size);
    }

    @GetMapping("/{productId}")
    public Products getProductsById(@PathVariable Long productId) {
        return productsService.getProductsById(productId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseDTOOutput> deleteUser(@PathVariable Long productId, HttpServletRequest request) {
        String userRole = (String) request.getAttribute("userRole");
        if(!Objects.equals(userRole, "admin")){
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setMsg("Unauthorized role");
            errorResponse.setStatus("failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        ResponseDTOOutput status = productsService.deleteById(productId);
        if ("success".equals(status.getStatus())) {
            return ResponseEntity.ok(status);
        }else {
            return ResponseEntity.badRequest().body(status);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDTOOutput> createProduct(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") Float price,
            @RequestParam("duration") Integer duration,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "imageStoreName", required = false) String imageName,
            HttpServletRequest request)
    {
        String userRole = (String) request.getAttribute("userRole");
        if(!Objects.equals(userRole, "admin")){
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setMsg("Unauthorized role");
            errorResponse.setStatus("failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            // Panggil service untuk menyimpan produk
            ResponseDTOOutput response = productsService.createProduct(title, description, price, duration, stock, image, imageName);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setMsg("Failed to save image");
            errorResponse.setStatus("failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setMsg(e.getMessage());
            errorResponse.setStatus("failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ResponseDTOOutput> updateProductions(
            @PathVariable Long productId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") Float price,
            @RequestParam("duration") Integer duration,
            @RequestParam("stock") Integer stock,
            @RequestParam(value = "prevImage", required = false) String prevImage,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "imageStoreName", required = false) String imageName,
            HttpServletRequest request)
    {
        String userRole = (String) request.getAttribute("userRole");
        if(!Objects.equals(userRole, "admin")){
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setMsg("Unauthorized role");
            errorResponse.setStatus("failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            ResponseDTOOutput response = productsService.updateProduct(title, description, price, duration, stock, prevImage, image, imageName, productId);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setMsg("Failed to save image");
            errorResponse.setStatus("failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            ResponseDTOOutput errorResponse = new ResponseDTOOutput();
            errorResponse.setMsg(e.getMessage());
            errorResponse.setStatus("failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @MessageMapping("/updateDataRealTime")
    @SendTo("/topic/updateDataRealTime")
    public ProductsDTO updateDataRealTime(){
        return productsService.getAllProducts("", null);
    }
}
