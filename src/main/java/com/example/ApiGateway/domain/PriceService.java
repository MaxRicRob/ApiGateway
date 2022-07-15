package com.example.ApiGateway.domain;

import com.example.ApiGateway.domain.entity.PriceRequest;
import com.example.ApiGateway.domain.entity.PriceResponse;

public interface PriceService {

    PriceResponse getPrice(PriceRequest priceRequest);
}
