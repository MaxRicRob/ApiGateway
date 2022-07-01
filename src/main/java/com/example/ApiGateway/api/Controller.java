package com.example.ApiGateway.api;


import com.example.ApiGateway.entity.DefaultProductResponse;
import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.CurrencyResponse;
import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponentResponse;
import com.example.ApiGateway.entity.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.example.ApiGateway.domain.ApiService;

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
    public ProductResponse deleteProduct(@PathVariable final String id) {
        return ProductResponse.from(apiService.deleteProduct(id));
    }

    @PostMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ProductResponse createProduct(@RequestBody final Product product) {
        return ProductResponse.from(apiService.createProduct(product));
    }

    @PutMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ProductResponse updateProduct(@RequestBody final Product product) {
        return ProductResponse.from(apiService.updateProduct(product));
    }

    @GetMapping(path = "/priceRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PriceResponse getPrice(@RequestBody final PriceRequest priceRequest) {
        return apiService.getPrice(priceRequest);
    }

    @GetMapping(path = "/currencyRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyResponse getCurrency(@RequestBody final CurrencyRequest currencyRequest) {
        return apiService.getCurrency(currencyRequest);
    }

}



