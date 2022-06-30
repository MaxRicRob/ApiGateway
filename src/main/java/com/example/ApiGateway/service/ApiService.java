package com.example.ApiGateway.service;

import com.example.ApiGateway.api.dto.CurrencyRequest;
import com.example.ApiGateway.api.dto.CurrencyResponse;
import com.example.ApiGateway.api.dto.DefaultProduct;
import com.example.ApiGateway.api.dto.PriceRequest;
import com.example.ApiGateway.api.dto.PriceResponse;
import com.example.ApiGateway.api.dto.Product;
import com.example.ApiGateway.api.dto.ProductComponent;
import com.example.ApiGateway.api.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiService {

    final ProductService productService;


    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
    }

    public List<DefaultProduct> getDefaultProducts() {
        return null;
    }

    public List<ProductComponent> getProductComponents() {
        return productService.getAllComponents();
    }

    public List<Product> getProductsFromUser(String userName) {
        return null;
    }

    public ProductResponse createProduct(Product product) {
        return null;
    }

    public ProductResponse updateProduct(Product product) {
        return null;
    }

    public PriceResponse getPrice(PriceRequest priceRequest) {
        return null;
    }

    public CurrencyResponse getCurrency(CurrencyRequest currencyRequest) {
        return null;
    }
}





