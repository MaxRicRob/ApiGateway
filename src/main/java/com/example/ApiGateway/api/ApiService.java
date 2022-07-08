package com.example.ApiGateway.api;

import com.example.ApiGateway.api.dto.CurrencyResponse;
import com.example.ApiGateway.api.dto.DefaultProductResponse;
import com.example.ApiGateway.api.dto.ProductComponentResponse;
import com.example.ApiGateway.api.dto.ProductResponse;
import com.example.ApiGateway.domain.CurrencyService;
import com.example.ApiGateway.domain.PriceService;
import com.example.ApiGateway.domain.ProductService;
import com.example.ApiGateway.domain.entity.CurrencyRequest;
import com.example.ApiGateway.domain.entity.PriceRequest;
import com.example.ApiGateway.domain.entity.PriceResponse;
import com.example.ApiGateway.domain.entity.Product;
import com.example.ApiGateway.domain.entity.ProductComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final ProductService productService;
    private final PriceService priceService;
    private final CurrencyService currencyService;

    public List<DefaultProductResponse> getDefaultProducts() {

        return productService.getDefaultProducts()
                .stream()
                .map(DefaultProductResponse::from)
                .collect(Collectors.toList());
    }

    public List<ProductComponentResponse> getProductComponents() {

        return productService.getAllComponents()
                .stream()
                .map(ProductComponentResponse::from)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsFromUser(String userName) {

        var products = productService.getProductsFromUser(userName)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        products.forEach(setTotalPrice());

        return products;
    }

    public ProductResponse deleteProduct(String id) {

        return ProductResponse.from(productService.deleteProduct(id));
    }

    public ProductResponse createProduct(Product product) {

        product.setId(UUID.randomUUID());
        return ProductResponse.from(
                productService.createProduct(product)
                );
    }

    public ProductResponse updateProduct(Product product) {

        return ProductResponse.from(productService.updateProduct(product));
    }

    public PriceResponse getFromPriceService(PriceRequest priceRequest) {

        return priceService.getPrice(priceRequest);
    }

    public CurrencyResponse getFromCurrencyService(CurrencyRequest currencyRequest) {

        return CurrencyResponse.from(currencyService.getCurrency(currencyRequest));
    }

    private Consumer<ProductResponse> setTotalPrice() {
        return p -> p.setTotalPrice(
                getFromPriceService(
                        new PriceRequest()
                                .setPrices(getComponentPrices(p))
                ).getTotalPrice()
        );
    }

    private List<Long> getComponentPrices(ProductResponse productResponse) {
        return productResponse.getComponents().stream()
                .map(ProductComponent::getPrice)
                .collect(Collectors.toList());
    }

}





