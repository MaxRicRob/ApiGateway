package com.example.ApiGateway.domain;

import com.example.ApiGateway.domain.entity.CurrencyRequest;

public interface CurrencyService {

    CurrencyRequest getCurrency(CurrencyRequest currencyRequest);
}
