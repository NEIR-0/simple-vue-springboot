package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.product.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    @Query("SELECT p FROM Products p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY p.createdAt DESC")
    Page<Products> findByTitleContainingIgnoreCaseAdminSite(String title, Pageable pageable);

    @Query("SELECT p FROM Products p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY p.createdAt DESC")
    List<Products> findByTitleContainingIgnoreCase(String title);
}
