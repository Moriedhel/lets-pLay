package com.letsplay.dto;

import java.math.BigDecimal;

import com.letsplay.model.Product;

public record ProductResponse(String id, String name, String description, BigDecimal price, int quantity, String ownerId) {

    public static ProductResponse fromProduct(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getOwnerId());
    }
}
