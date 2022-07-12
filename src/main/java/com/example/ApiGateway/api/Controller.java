package com.example.ApiGateway.api;


import com.example.ApiGateway.domain.ApiService;
import com.example.ApiGateway.domain.entity.CurrencyRequest;
import com.example.ApiGateway.domain.entity.DefaultProduct;
import com.example.ApiGateway.domain.entity.PriceRequest;
import com.example.ApiGateway.domain.entity.PriceResponse;
import com.example.ApiGateway.domain.entity.Product;
import com.example.ApiGateway.domain.entity.ProductComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin("http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
@RestController
public class Controller {

    private final ApiService apiService;

    @GetMapping("/defaultProducts")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Default Products from Warehouse.")
    public List<DefaultProduct> getDefaultProducts() {

        log.info("get DefaultProducts Endpoint called");
        return apiService.getDefaultProducts();
    }

    @GetMapping("/productComponents")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Default Product Components from Warehouse.")
    public List<ProductComponent> getProductComponents() {

        log.info("get ProductComponents Endpoint called");
        return apiService.getProductComponents();
    }

    @GetMapping("/products/{userName}")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Products from user by username.")
    public List<Product> getProductsFromUser(
            @Parameter(description = "Name of the user")
            @PathVariable final String userName) {

        log.info("get Products from User {} Endpoint called", userName);
        return apiService.getProductsFromUser(userName);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Delete a product by its id.")
    public Product deleteProduct(
            @Parameter(description = "UUID of the product")
            @PathVariable final String id) {

        log.info("get delete product with id {} Endpoint called", id);
        return apiService.deleteProduct(id);
    }

    @PostMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a product.")
    public Product createProduct(
            @Parameter(description = "Product that needs to be created")
            @RequestBody final Product product) {

        log.info("post Product for product {} Endpoint called", product.getName());
        return apiService.createProduct(product);
    }

    @PutMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Update a product.")
    public Product updateProduct(
            @Parameter(description = "Updated Product")
            @RequestBody final Product product) {

        log.info("update Product for product {} Endpoint called", product.getId());
        return apiService.updateProduct(product);
    }

    @GetMapping(path = "/priceRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the price for a product.")
    @ResponseStatus(OK)
    public PriceResponse getPrice(
            @RequestBody final PriceRequest priceRequest) {

        log.info("get priceRequest Endpoint called");
        return apiService.getFromPriceService(priceRequest);
    }

    @GetMapping(path = "/currencyRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the price for a product in a specific currency of your choice")
    @ResponseStatus(OK)
    public CurrencyRequest getCurrency(
            @Parameter(description = "allowed currencies: EURO, MXN, USD, CAD, YEN, POUND")
            @RequestBody final CurrencyRequest currencyRequest) {

        log.info("get currencyRequest Endpoint called");
        return apiService.getFromCurrencyService(currencyRequest);
    }

}



