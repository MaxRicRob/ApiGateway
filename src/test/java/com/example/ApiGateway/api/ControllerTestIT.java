package com.example.ApiGateway.api;

import com.example.ApiGateway.domain.ApiService;
import com.example.ApiGateway.entity.CurrencyRequest;
import com.example.ApiGateway.entity.DefaultProduct;
import com.example.ApiGateway.entity.PriceRequest;
import com.example.ApiGateway.entity.PriceResponse;
import com.example.ApiGateway.entity.Product;
import com.example.ApiGateway.entity.ProductComponent;
import com.example.ApiGateway.error.ErrorResponseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static com.example.ApiGateway.entity.Currency.MXN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTestIT {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    private final HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    private final CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApiService apiService;

    @Test
    @WithMockUser(roles = "user")
    void get_default_products_status_ok() {

        try {
            var defaultProducts = List.of(
                    new DefaultProduct().setName("testProduct1"),
                    new DefaultProduct().setName("testProduct2")
            );
            when(apiService.getDefaultProducts()).thenReturn(defaultProducts);

            var mockMvcResult = mockMvc.perform(get("/defaultProducts"))
                    .andExpect(status().isOk())
                    .andReturn();

            List<DefaultProduct> actualResults =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertThat(actualResults.size()).isEqualTo(2);
            assertThat(actualResults.get(0).getName()).isEqualTo("testProduct1");
            assertThat(actualResults.get(1).getName()).isEqualTo("testProduct2");
            verify(apiService).getDefaultProducts();

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void get_default_products_status_bad_request() {

        try {
            when(apiService.getDefaultProducts()).thenThrow(ErrorResponseException.class);

            mockMvc.perform(get("/defaultProducts"))
                    .andExpect(status().isBadRequest());

            verify(apiService).getDefaultProducts();

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void get_components_status_ok() {

        try {
            var components = List.of(
                    new ProductComponent().setName("testComponent1"),
                    new ProductComponent().setName("testComponent2")
            );
            when(apiService.getProductComponents()).thenReturn(components);

            var mockMvcResult = mockMvc.perform(get("/productComponents"))
                    .andExpect(status().isOk())
                    .andReturn();

            List<ProductComponent> actualResults =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertThat(actualResults.size()).isEqualTo(2);
            assertThat(actualResults.get(0).getName()).isEqualTo("testComponent1");
            assertThat(actualResults.get(1).getName()).isEqualTo("testComponent2");
            verify(apiService).getProductComponents();

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void get_components_status_bad_request() {

        try {
            when(apiService.getProductComponents()).thenThrow(ErrorResponseException.class);

            mockMvc.perform(get("/productComponents"))
                    .andExpect(status().isBadRequest());

            verify(apiService).getProductComponents();

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void get_products_from_user_status_ok() {

        try {
            var products = List.of(
                    new Product().setName("testProduct1"),
                    new Product().setName("testProduct2")
            );
            when(apiService.getProductsFromUser("testUser")).thenReturn(products);

            var mockMvcResult = mockMvc.perform(get("/products/testUser"))
                    .andExpect(status().isOk())
                    .andReturn();

            List<Product> actualResults =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertThat(actualResults.size()).isEqualTo(2);
            assertThat(actualResults.get(0).getName()).isEqualTo("testProduct1");
            assertThat(actualResults.get(1).getName()).isEqualTo("testProduct2");
            verify(apiService).getProductsFromUser(eq("testUser"));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void get_products_from_user_status_bad_request() {

        try {
            when(apiService.getProductsFromUser("testUser")).thenThrow(ErrorResponseException.class);

            mockMvc.perform(get("/products/testUser"))
                    .andExpect(status().isBadRequest());

            verify(apiService).getProductsFromUser(eq("testUser"));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void delete_status_ok() {

        try {
            var id = UUID.randomUUID().toString();
            when(apiService.deleteProduct(id)).thenReturn("deleted");

            var mockMvcResult = mockMvc.perform(delete("/products/" + id)
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isOk())
                    .andReturn();

            String actualResult = mockMvcResult.getResponse().getContentAsString();
            assertThat(actualResult).isEqualTo("deleted");
            verify(apiService).deleteProduct(eq(id));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void delete_status_bad_request() {

        try {
            var id = UUID.randomUUID().toString();
            when(apiService.deleteProduct(id)).thenThrow(ErrorResponseException.class);

            mockMvc.perform(delete("/products/" + id)
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isBadRequest());

            verify(apiService).deleteProduct(eq(id));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void post_product_status_created() {

        try {
            var product = getTestProduct();
            when(apiService.createProduct(any(Product.class))).thenReturn(product);

            var mockMvcResult = mockMvc.perform(post("/products/")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(product))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isCreated())
                    .andReturn();

            Product actualResult =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertThat(actualResult.getName()).isEqualTo("test");
            assertThat(actualResult.getUserName()).isEqualTo("test");
            assertThat(actualResult.getComponents().get(0).getName()).isEqualTo("test");
            verify(apiService).createProduct(any(Product.class));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void post_product_status_bad_request() {

        try {
            when(apiService.createProduct(any(Product.class))).thenThrow(ErrorResponseException.class);

            mockMvc.perform(post("/products/")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getTestProduct()))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isBadRequest());

            verify(apiService).createProduct(any(Product.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void put_product_status_ok() {

        try {
            var product = getTestProduct();
            when(apiService.updateProduct(any(Product.class))).thenReturn(product);

            var mockMvcResult = mockMvc.perform(put("/products/")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(product))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isOk())
                    .andReturn();

            Product actualResult =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertThat(actualResult.getName()).isEqualTo("test");
            assertThat(actualResult.getUserName()).isEqualTo("test");
            assertThat(actualResult.getComponents().get(0).getName()).isEqualTo("test");
            verify(apiService).updateProduct(any(Product.class));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void put_product_status_bad_request() {

        try {
            when(apiService.updateProduct(any(Product.class))).thenThrow(ErrorResponseException.class);

            mockMvc.perform(put("/products/")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getTestProduct()))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isBadRequest());

            verify(apiService).updateProduct(any(Product.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void post_price_request_status_ok() {

        try {
            var priceResponse = new PriceResponse()
                    .setTotalPrice(100);
            when(apiService.getFromPriceService(any(PriceRequest.class))).thenReturn(priceResponse);

            var mockMvcResult = mockMvc.perform(post("/priceRequest")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getPriceRequest()))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isOk())
                    .andReturn();

            PriceResponse actualResult =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertThat(actualResult.getTotalPrice()).isEqualTo(100);
            verify(apiService).getFromPriceService(any(PriceRequest.class));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void post_price_request_status_bad_request() {

        try {
            when(apiService.getFromPriceService(any(PriceRequest.class)))
                    .thenThrow(ErrorResponseException.class);

            mockMvc.perform(post("/priceRequest")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getPriceRequest()))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            verify(apiService).getFromPriceService(any(PriceRequest.class));

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    @WithMockUser(roles = "user")
    void post_currency_request_status_ok() {

        try {
            var currencyRequest = getCurrencyRequest();
            when(apiService.getFromCurrencyService(any(CurrencyRequest.class))).thenReturn(currencyRequest);

            var mockMvcResult = mockMvc.perform(post("/currencyRequest")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(currencyRequest))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isOk())
                    .andReturn();

            CurrencyRequest actualResult =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>() {
                    });
            assertThat(actualResult.getTotalPrice()).isEqualTo(100);
            assertThat(actualResult.getWantedCurrency()).isEqualTo(MXN);
            verify(apiService).getFromCurrencyService(any(CurrencyRequest.class));

        } catch (Exception e) {
            fail();
        }
    }


    @Test
    @WithMockUser(roles = "user")
    void post_currency_request_status_bad_request() {

        try {
            when(apiService.getFromCurrencyService(any(CurrencyRequest.class)))
                    .thenThrow(ErrorResponseException.class);

            mockMvc.perform(post("/currencyRequest")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getCurrencyRequest()))
                            .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                            .param(csrfToken.getParameterName(), csrfToken.getToken()))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            verify(apiService).getFromCurrencyService(any(CurrencyRequest.class));

        } catch (Exception e) {
            fail();
        }
    }


    private Product getTestProduct() {
        return new Product()
                .setName("test")
                .setUserName("test")
                .setComponents(
                        List.of(
                                new ProductComponent()
                                        .setName("test")
                        )
                );
    }

    private PriceRequest getPriceRequest() {
        return new PriceRequest()
                .setPrices(
                        List.of(10L, 90L)
                );
    }

    private CurrencyRequest getCurrencyRequest() {
        return new CurrencyRequest()
                .setWantedCurrency(MXN)
                .setTotalPrice(100);
    }

}
