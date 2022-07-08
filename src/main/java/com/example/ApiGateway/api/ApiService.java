package com.example.ApiGateway.api;

import com.example.ApiGateway.domain.CurrencyService;
import com.example.ApiGateway.domain.PriceService;
import com.example.ApiGateway.domain.ProductService;
import com.example.ApiGateway.domain.entity.CurrencyRequest;
import com.example.ApiGateway.domain.entity.DefaultProduct;
import com.example.ApiGateway.domain.entity.PriceRequest;
import com.example.ApiGateway.domain.entity.PriceResponse;
import com.example.ApiGateway.domain.entity.Product;
import com.example.ApiGateway.domain.entity.ProductComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final ProductService productService;
    private final PriceService priceService;
    private final CurrencyService currencyService;

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

    public PriceResponse getFromPriceService(PriceRequest priceRequest) {

        return priceService.getPrice(priceRequest);
    }

    public CurrencyRequest getFromCurrencyService(CurrencyRequest currencyRequest) {

        return currencyService.getCurrency(currencyRequest);
    }
}





