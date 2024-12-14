package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    @Query("SELECT p FROM Products p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Products> findByTitleContainingIgnoreCase(String title);
}
