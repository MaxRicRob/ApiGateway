package com.example.ApiGateway.api;

import com.example.ApiGateway.domain.ApiService;
import com.example.ApiGateway.domain.entity.DefaultProduct;
import com.example.ApiGateway.domain.entity.ProductComponent;
import com.example.ApiGateway.error.ErrorResponseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(Controller.class)
public class ControllerTestIT {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApiService apiService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void get_default_products_status_ok() {

        try {
            List<DefaultProduct> defaultProducts = List.of(
                    new DefaultProduct().setName("testProduct1"),
                    new DefaultProduct().setName("testProduct2")
            );
            when(apiService.getDefaultProducts()).thenReturn(defaultProducts);

            var mockMvcResult = mockMvc.perform(get("/defaultProducts"))
                    .andExpect(status().isOk())
                    .andReturn();

            List<DefaultProduct> actualResults =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
            assertThat(actualResults.size()).isEqualTo(2);
            assertThat(actualResults.get(0).getName()).isEqualTo("testProduct1");
            assertThat(actualResults.get(1).getName()).isEqualTo("testProduct2");
            verify(apiService).getDefaultProducts();

        } catch (Exception e) {
            fail();
        }
    }

    @Test
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
    void get_components_status_ok() {

        try {
            List<ProductComponent> components = List.of(
                    new ProductComponent().setName("testComponent1"),
                    new ProductComponent().setName("testComponent2")
            );
            when(apiService.getProductComponents()).thenReturn(components);

            var mockMvcResult = mockMvc.perform(get("/productComponents"))
                    .andExpect(status().isOk())
                    .andReturn();

            List<ProductComponent> actualResults =
                    objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), new TypeReference<>(){});
            assertThat(actualResults.size()).isEqualTo(2);
            assertThat(actualResults.get(0).getName()).isEqualTo("testComponent1");
            assertThat(actualResults.get(1).getName()).isEqualTo("testComponent2");
            verify(apiService).getProductComponents();

        } catch (Exception e) {
            fail();
        }
    }

    @Test
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


}
