package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.CurrencyResponse;
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
public class ApiService {

    final ProductService productService;
    final PriceService priceService;
    final CurrencyService currencyService;


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
        return currencyService.getCurrency(currencyRequest);
    }

}





