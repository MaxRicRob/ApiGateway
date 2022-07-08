package com.example.ApiGateway.api.dto;

import com.example.ApiGateway.domain.entity.Product;
import com.example.ApiGateway.domain.entity.ProductComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductResponse {

    private UUID id;
    private String name;
    private String userName;
    private long totalPrice;
    private List<ProductComponent> components;

    public static ProductResponse from(Product product) {
        if (product == null) {
            return new ProductResponse();
        }
        return new ProductResponse()
                .setId(product.getId())
                .setName(product.getName())
                .setUserName(product.getUserName())
                .setComponents(product.getComponents());
    }
}
