package com.example.ApiGateway.domain;


import com.example.ApiGateway.api.dto.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CurrencyRequest {

    private int id;
    private long totalPrice;
    private Currency wantedCurrency;


}
