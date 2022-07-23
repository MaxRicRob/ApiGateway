package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.CurrencyRequest;

public interface CurrencyService {

    CurrencyRequest getCurrency(CurrencyRequest currencyRequest);
}
