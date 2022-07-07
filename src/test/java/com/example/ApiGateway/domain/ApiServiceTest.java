package com.example.ApiGateway.domain;

import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

    @InjectMocks
    private ApiService apiService;

    @Mock
    private ProductService productService;
    @Mock
    private PriceService priceService;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private Product product;
    @Mock
    private PriceRequest priceRequest;
    @Mock
    private CurrencyRequest currencyRequest;

    @Test
    void getDefaultProducts() {
        apiService.getDefaultProducts();

        verify(productService).getDefaultProducts();
    }

    @Test
    void getProductComponents() {
        apiService.getProductComponents();

        verify(productService).getAllComponents();
    }

    @Test
    void getProductsFromUser() {
        apiService.getProductsFromUser("userName");

        verify(productService).getProductsFromUser("userName");
    }

    @Test
    void deleteProduct() {
        apiService.deleteProduct("1");

        verify(productService).deleteProduct("1");
    }

    @Test
    void createProduct() {
        apiService.createProduct(product);

        verify(productService).createProduct(product);
    }

    @Test
    void updateProduct() {
        apiService.updateProduct(product);

        verify(productService).updateProduct(product);
    }

    @Test
    void getPrice() {
        apiService.getPrice(priceRequest);

        verify(priceService).getPrice(priceRequest);
    }

    @Test
    void getCurrency() {
        apiService.getCurrency(currencyRequest);

        verify(currencyService).getCurrency(currencyRequest);
    }
}
