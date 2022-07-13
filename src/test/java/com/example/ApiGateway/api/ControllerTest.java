package com.example.ApiGateway.api;

import com.example.ApiGateway.domain.ApiService;
import com.example.ApiGateway.domain.entity.CurrencyRequest;
import com.example.ApiGateway.domain.entity.PriceRequest;
import com.example.ApiGateway.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ControllerTest {

    @InjectMocks
    private Controller controller;
    @Mock
    private ApiService apiService;
    @Mock
    private Product product;
    @Mock
    private PriceRequest priceRequest;
    @Mock
    private CurrencyRequest currencyRequest;

    @Test
    void getDefaultProducts() {

        controller.getDefaultProducts();

        verify(apiService).getDefaultProducts();
    }

    @Test
    void getProductComponents() {

        controller.getProductComponents();

        verify(apiService).getProductComponents();
    }

    @Test
    void getProductsFromUser() {

        controller.getProductsFromUser("test");

        verify(apiService).getProductsFromUser(eq("test"));
    }

    @Test
    void deleteProduct() {
        var uuid = UUID.randomUUID();

        controller.deleteProduct(uuid.toString());

        verify(apiService).deleteProduct(eq(uuid.toString()));
    }

    @Test
    void createProduct() {

        controller.createProduct(product);

        verify(apiService).createProduct(product);

    }

    @Test
    void updateProduct() {

        controller.updateProduct(product);

        verify(apiService).updateProduct(product);
    }

    @Test
    void getPrice() {

        controller.getPrice(priceRequest);

        verify(apiService).getFromPriceService(priceRequest);

    }

    @Test
    void getCurrency() {

        controller.getCurrency(currencyRequest);

        verify(apiService).getFromCurrencyService(currencyRequest);
    }
}
