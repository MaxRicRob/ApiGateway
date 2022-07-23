package com.example.ApiGateway.domain.impl;


import com.example.ApiGateway.domain.ApiService;
import com.example.ApiGateway.domain.CurrencyService;
import com.example.ApiGateway.domain.PriceService;
import com.example.ApiGateway.domain.ProductService;
import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.DefaultProduct;
import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ApiServiceImpl implements ApiService {

    private final ProductService productService;
    private final PriceService priceService;
    private final CurrencyService currencyService;

    @Override
    public List<DefaultProduct> getDefaultProducts() {

        return productService.getDefaultProducts();
    }

    @Override
    public List<ProductComponent> getProductComponents() {

        return productService.getAllComponents();
    }

    @Override
    public List<Product> getProductsFromUser(String userName) {

        return productService.getProductsFromUser(userName);
    }

    @Override
    public String deleteProduct(String id) {

        return productService.deleteProduct(id);
    }

    @Override
    public Product createProduct(Product product) {

        return productService.createProduct(product);
    }

    @Override
    public Product updateProduct(Product product) {

        return productService.updateProduct(product);
    }

    @Override
    public PriceResponse getFromPriceService(PriceRequest priceRequest) {

        return priceService.getPrice(priceRequest);
    }

    @Override
    public CurrencyRequest getFromCurrencyService(CurrencyRequest currencyRequest) {

        return currencyService.getCurrency(currencyRequest);
    }
}





