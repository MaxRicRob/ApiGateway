package com.example.ApiGateway.domain;

import com.example.ApiGateway.domain.ProductComponent;
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
public class DefaultProduct {

    private int id;
    private String name;
    private List<ProductComponent> components;

}
