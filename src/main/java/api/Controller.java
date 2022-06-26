package api;


import domain.DefaultProduct;
import domain.Product;
import domain.ProductComponent;
import domain.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import service.ApiService;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
public class Controller {

    private final ApiService apiService;

    @GetMapping("/defaultProducts")
    @ResponseStatus(OK)
    public List<DefaultProduct> getDefaultProducts() {
        return apiService.getDefaultProducts();
    }

    @GetMapping("/productComponents")
    @ResponseStatus(OK)
    public List<ProductComponent> getProductComponents() {
        return apiService.getProductComponents();
    }

    @GetMapping("/products/{userName}")
    @ResponseStatus(OK)
    public List<Product> getProductsFromUser(@PathVariable final String userName) {
        return apiService.getProductsFromUser(userName);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable final String id) {
        apiService.delete(id);
    }

    @PostMapping("/products")
    @ResponseStatus(CREATED)
    public ProductResponse createProduct(@RequestBody final Product product) {
        return apiService.createProduct(product);
    }

    @PutMapping("/products")
    @ResponseStatus(CREATED)
    public ProductResponse updateProduct(@RequestBody final Product product) {
        return apiService.updateProduct(product);
    }


}



