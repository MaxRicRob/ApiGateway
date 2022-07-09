package com.example.ApiGateway.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class ProductComponent {

    private int id;
    private String name;
    private long price;
    private int weight;
    private String color;
    private String origin;
    @Schema(minimum = "1", maximum = "10")
    private int awesomeness;
    private String farmer;
    private boolean organic;
    private int calories;

}
