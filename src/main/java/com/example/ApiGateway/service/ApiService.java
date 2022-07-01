package com.example.ApiGateway.service;

import com.example.ApiGateway.domain.CurrencyRequest;
import com.example.ApiGateway.api.dto.CurrencyResponse;
import com.example.ApiGateway.domain.DefaultProduct;
import com.example.ApiGateway.domain.PriceRequest;
import com.example.ApiGateway.api.dto.PriceResponse;
import com.example.ApiGateway.domain.PriceService;
import com.example.ApiGateway.domain.Product;
import com.example.ApiGateway.domain.ProductComponent;
import com.example.ApiGateway.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService {

    final ProductService productService;
    final PriceService priceService;


    public List<DefaultProduct> getDefaultProducts() {
        return productService.getDefaultProducts();
    }

    public List<ProductComponent> getProductComponents() {
        return productService.getAllComponents();
    }

    public List<Product> getProductsFromUser(String userName) {
        return productService.getProductsFromUser(userName);
    }

    public Product deleteProduct(String id) {
        return productService.deleteProduct(id);
    }

    public Product createProduct(Product product) {
        return productService.createProduct(product);
    }

    public Product updateProduct(Product product) {
        return productService.updateProduct(product);
    }

    public PriceResponse getPrice(PriceRequest priceRequest) {
        return priceService.getPrice(priceRequest);
    }

    public CurrencyResponse getCurrency(CurrencyRequest currencyRequest) {
        return null;
    }

}





