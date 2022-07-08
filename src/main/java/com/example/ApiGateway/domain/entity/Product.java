package com.example.ApiGateway.domain.entity;

import com.example.ApiGateway.api.dto.ProductResponse;
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
public class Product {

    private UUID id;
    private String name;
    private String userName;
    private List<ProductComponent> components;

    public static Product from(ProductResponse productResponse) {
        return new Product()
                .setId(productResponse.getId())
                .setName(productResponse.getName())
                .setUserName(productResponse.getUserName())
                .setComponents(productResponse.getComponents());
    }
}
