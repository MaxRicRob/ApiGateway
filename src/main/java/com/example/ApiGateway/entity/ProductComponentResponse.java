package com.example.ApiGateway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductComponentResponse {

    private int id;
    private String name;
    private long price;
    private int weight;
    private String color;
    private String origin;
    private int awesomeness;
    private String farmer;
    private boolean organic;
    private int calories;

    public static ProductComponentResponse from(ProductComponent productComponent) {
        return new ProductComponentResponse()
                .setId(productComponent.getId())
                .setName(productComponent.getName())
                .setAwesomeness(productComponent.getAwesomeness())
                .setCalories(productComponent.getCalories())
                .setColor(productComponent.getColor())
                .setFarmer(productComponent.getFarmer())
                .setOrigin(productComponent.getOrigin())
                .setPrice(productComponent.getPrice())
                .setOrganic(productComponent.isOrganic())
                .setWeight(productComponent.getWeight());
    }

}
