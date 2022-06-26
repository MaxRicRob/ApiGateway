package service;

import domain.DefaultProduct;
import domain.Product;
import domain.ProductComponent;
import domain.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApiService {


    public void delete(String id) {
        UUID uuid = UUID.fromString(id);
    }

    public List<DefaultProduct> getDefaultProducts() {
        return null;
    }

    public List<ProductComponent> getProductComponents() {
        return null;
    }

    public List<Product> getProductsFromUser(String userName) {
        return null;
    }

    public ProductResponse createProduct(Product product) {
        return null;
    }

    public ProductResponse updateProduct(Product product) {
        return null;
    }
}





