package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;

public interface PriceService {

    PriceResponse getPrice(PriceRequest priceRequest);
}
