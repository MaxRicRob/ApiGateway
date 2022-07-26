package com.example.ApiGateway.api;


import com.example.ApiGateway.domain.ApiService;
import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.DefaultProduct;
import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController
public class Controller {

    private final ApiService apiService;

    @PreAuthorize("hasRole('user')")
    @GetMapping("/defaultProducts")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Default Products from Warehouse.")
    public ResponseEntity<List<DefaultProduct>> getDefaultProducts() {

        log.info("get DefaultProducts Endpoint called");
        //return status(OK).body(apiService.getDefaultProducts());
        return status(OK).body(null);
    }

    @PreAuthorize("hasRole('user')")
    @GetMapping("/productComponents")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Default Product Components from Warehouse.")
    public ResponseEntity<List<ProductComponent>> getProductComponents() {

        log.info("get ProductComponents Endpoint called");
//        return status(OK).body(apiService.getProductComponents());
        return status(OK).body(null);
    }

    @PreAuthorize("hasRole('user')")
    @GetMapping("/products/{userName}")
    @ResponseStatus(OK)
    @Operation(summary = "Get all Products from user by username.")
    public ResponseEntity<List<Product>> getProductsFromUser(
            @Parameter(description = "Name of the user")
            @PathVariable final String userName) {

        log.info("get Products from User {} Endpoint called", userName);
        return status(OK).body(apiService.getProductsFromUser(userName));
    }

    @PreAuthorize("hasRole('user')")
    @DeleteMapping("/products/{id}")
    @ResponseStatus(OK)
    @Operation(summary = "Delete a product by its id.")
    public ResponseEntity<String> deleteProduct(
            @Parameter(description = "UUID of the product")
            @PathVariable final String id) {

        log.info("get delete product with id {} Endpoint called", id);
        return status(OK).body(apiService.deleteProduct(id));
    }

    @PreAuthorize("hasRole('user')")
    @PostMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a product.")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product that needs to be created")
            @RequestBody final Product product) {

        log.info("post Product for product {} Endpoint called", product.getName());
        return status(CREATED).body(apiService.createProduct(product));
    }

    @PreAuthorize("hasRole('user')")
    @PutMapping(path = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @Operation(summary = "Update a product.")
    public ResponseEntity<Product> updateProduct(
            @Parameter(description = "Updated Product")
            @RequestBody final Product product) {

        log.info("update Product for product {} Endpoint called", product.getId());
        return status(OK).body(apiService.updateProduct(product));
    }

    @PreAuthorize("hasRole('user')")
    @PostMapping(path = "/priceRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the price for a product.")
    @ResponseStatus(OK)
    public ResponseEntity<PriceResponse> getPrice(
            @RequestBody final PriceRequest priceRequest) {

        log.info("get priceRequest Endpoint called");
        return status(OK).body(apiService.getFromPriceService(priceRequest));
    }

    @PreAuthorize("hasRole('user')")
    @PostMapping(path = "/currencyRequest", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get the price for a product or component in a specific currency of your choice")
    @ResponseStatus(OK)
    public ResponseEntity<CurrencyRequest> getCurrency(
            @Parameter(description = "allowed currencies: EUR, MXN, USD, CAD, YEN, PND")
            @RequestBody final CurrencyRequest currencyRequest) {

        log.info("get currencyRequest Endpoint called");
        return status(OK).body(apiService.getFromCurrencyService(currencyRequest));
    }

}



