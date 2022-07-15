package com.example.ApiGateway.domain;

import com.example.ApiGateway.domain.entity.DefaultProduct;
import com.example.ApiGateway.domain.entity.Product;
import com.example.ApiGateway.domain.entity.ProductComponent;

import java.util.List;


public interface ProductService {

    List<ProductComponent> getAllComponents();

    List<DefaultProduct> getDefaultProducts();

    List<Product> getProductsFromUser(String userName);

    String deleteProduct(String id);

    Product createProduct(Product product);

    Product updateProduct(Product product);

}
