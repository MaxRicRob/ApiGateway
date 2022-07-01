package com.example.ApiGateway.api;


import com.example.ApiGateway.api.dto.DefaultProductResponse;
import com.example.ApiGateway.domain.CurrencyRequest;
import com.example.ApiGateway.api.dto.CurrencyResponse;
import com.example.ApiGateway.domain.DefaultProduct;
import com.example.ApiGateway.domain.PriceRequest;
import com.example.ApiGateway.api.dto.PriceResponse;
import com.example.ApiGateway.domain.Product;
import com.example.ApiGateway.api.dto.ProductComponentResponse;
import com.example.ApiGateway.api.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.ApiGateway.service.ApiService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class Controller {

    private final ApiService apiService;

    @GetMapping("/defaultProducts")
    @ResponseStatus(OK)
    public List<DefaultProductResponse> getDefaultProducts() {
        return apiService.getDefaultProducts().stream()
                .map(DefaultProductResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/productComponents")
    @ResponseStatus(OK)
    public List<ProductComponentResponse> getProductComponents() {
        return apiService.getProductComponents().stream()
                .map(ProductComponentResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/products/{userName}")
    @ResponseStatus(OK)
    public List<ProductResponse> getProductsFromUser(@PathVariable final String userName) {
        return apiService.getProductsFromUser(userName).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteProduct(@PathVariable final String id) {
        apiService.deleteProduct(id);
    }

    @PostMapping("/products")
    @ResponseStatus(CREATED)
    public ProductResponse createProduct(@RequestBody final Product product) {
        return apiService.createProduct(product);
    }

    @PutMapping("/products")
    @ResponseStatus(CREATED)
    public ProductResponse updateProduct(@RequestBody final Product product) {
        return apiService.updateProduct(product);
    }

    @GetMapping("/priceRequest")
    public PriceResponse getPrice(@RequestBody final PriceRequest priceRequest) {
        return apiService.getPrice(priceRequest);
    }

    @GetMapping("/currencyRequest")
    public CurrencyResponse getCurrency(@RequestBody final CurrencyRequest currencyRequest) {
        return apiService.getCurrency(currencyRequest);
    }



}



