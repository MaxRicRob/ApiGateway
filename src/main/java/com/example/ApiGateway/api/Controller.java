package com.example.ApiGateway.api;


import com.example.ApiGateway.entity.DefaultProductResponse;
import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.CurrencyResponse;
import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponentResponse;
import com.example.ApiGateway.entity.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "Get all Default Products from Warehouse.")
    public List<DefaultProductResponse> getDefaultProducts() {
        return apiService.getDefaultProducts().stream()
                .map(DefaultProductResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/productComponents")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Default Product Components from Warehouse.")
    public List<ProductComponentResponse> getProductComponents() {
        return apiService.getProductComponents().stream()
                .map(ProductComponentResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/products/{userName}")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Products from user by username.")
    public List<ProductResponse> getProductsFromUser(
            @Parameter(description = "Name of the user")
            @PathVariable final String userName) {
        return apiService.getProductsFromUser(userName).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a product by its id.")
    public ProductResponse deleteProduct(
            @Parameter(description = "UUID of the product")
            @PathVariable final String id) {
        return ProductResponse.from(apiService.deleteProduct(id));
    }

    @PostMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a product.")
    public ProductResponse createProduct(
            @Parameter(description = "Product that needs to be created")
            @RequestBody final Product product) {
        return ProductResponse.from(apiService.createProduct(product));
    }

    @PutMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @Operation(summary = "Update a product.")
    public ProductResponse updateProduct(
            @Parameter(description = "Updated Product")
            @RequestBody final Product product) {
        return ProductResponse.from(apiService.updateProduct(product));
    }

    @GetMapping(path = "/priceRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the price for a product.")
    public PriceResponse getPrice(
            @RequestBody final PriceRequest priceRequest) {
        return apiService.getPrice(priceRequest);
    }

    @GetMapping(path = "/currencyRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the price for a product in a specific currency of your choice")
    public CurrencyResponse getCurrency(
            @Parameter(description = "allowed currencies: EURO, MXN, USD, CAD, YEN, POUND")
            @RequestBody final CurrencyRequest currencyRequest) {
        return apiService.getCurrency(currencyRequest);
    }

}



