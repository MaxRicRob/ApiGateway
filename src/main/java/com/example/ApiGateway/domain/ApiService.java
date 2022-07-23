package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.DefaultProduct;
import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponent;

import java.util.List;

public interface ApiService {

    List<DefaultProduct> getDefaultProducts();

    List<ProductComponent> getProductComponents();

    List<Product> getProductsFromUser(String userName);

    String deleteProduct(String id);

    Product createProduct(Product product);

    Product updateProduct(Product product);

    PriceResponse getFromPriceService(PriceRequest priceRequest);

    CurrencyRequest getFromCurrencyService(CurrencyRequest currencyRequest);
}



