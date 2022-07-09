package com.example.ApiGateway.domain.entity;


import io.swagger.v3.oas.annotations.media.Schema;
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

    private long totalPrice;
    @Schema(allowableValues = {"EURO", "MXN", "USD", "CAD", "YEN", "POUND"})
    private Currency wantedCurrency;

}
