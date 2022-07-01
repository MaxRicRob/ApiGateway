package com.example.ApiGateway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DefaultProductResponse {

    private int id;
    private String name;
    private List<ProductComponent> components;

    public static DefaultProductResponse from(DefaultProduct defaultProduct) {
        return new DefaultProductResponse()
                .setId(defaultProduct.getId())
                .setName(defaultProduct.getName())
                .setComponents(defaultProduct.getComponents());
    }
}
