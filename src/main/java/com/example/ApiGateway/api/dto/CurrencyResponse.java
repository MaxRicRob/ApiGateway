package com.example.ApiGateway.api.dto;


import com.example.ApiGateway.domain.entity.Currency;
import com.example.ApiGateway.domain.entity.CurrencyRequest;
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
public class CurrencyResponse {

    private long totalPrice;
    private Currency wantedCurrency;

    public static CurrencyResponse from(CurrencyRequest currencyRequest) {
        if (currencyRequest == null) {
            return new CurrencyResponse();
        }
        return new CurrencyResponse()
                .setWantedCurrency(currencyRequest.getWantedCurrency())
                .setTotalPrice(currencyRequest.getTotalPrice());
    }

}
