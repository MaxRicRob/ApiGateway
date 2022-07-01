package com.example.ApiGateway.service;

import com.example.ApiGateway.domain.CurrencyRequest;
import com.example.ApiGateway.api.dto.CurrencyResponse;
import com.example.ApiGateway.domain.DefaultProduct;
import com.example.ApiGateway.domain.PriceRequest;
import com.example.ApiGateway.api.dto.PriceResponse;
import com.example.ApiGateway.domain.Product;
import com.example.ApiGateway.domain.ProductComponent;
import com.example.ApiGateway.api.dto.ProductResponse;
import com.example.ApiGateway.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiService {

    final ProductService productService;


    public List<DefaultProduct> getDefaultProducts() {
        return productService.getDefaultProducts();
    }

    public List<ProductComponent> getProductComponents() {
        return productService.getAllComponents();
    }

    public List<Product> getProductsFromUser(String userName) {
        return productService.getProductsFromUser(userName);
    }

    public void deleteProduct(String id) {
        var uuid = UUID.fromString(id);
        productService.deleteProduct(uuid);
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





