package com.restApi.RestAPI.repository;

import com.restApi.RestAPI.model.product.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<Products, Long> {
}
