package com.restApi.RestAPI.dto;

import com.restApi.RestAPI.model.product.Products;

import java.util.List;

public class ProductsDTO {
    private List<Products> product;
    private List<Integer> durations;
    private Integer totalPages;

    public ProductsDTO(List<Products> product, List<Integer> durations, Integer totalPages) {
        this.product = product;
        this.durations = durations;
        this.totalPages = totalPages;
    }

    public List<Products> getProduct() {
        return product;
    }

    public void setProduct(List<Products> product) {
        this.product = product;
    }

    public List<Integer> getDurations() {
        return durations;
    }

    public void setDurations(List<Integer> durations) {
        this.durations = durations;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void settTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "ProductsDTO{" +
                "product=" + product +
                ", durations=" + durations +
                ", totalPages=" + totalPages +
                '}';
    }
}
