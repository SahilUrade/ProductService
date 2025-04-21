package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreResponseDTO;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {
    private RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    private Product convertFakeStoreResponseToProduct(FakeStoreResponseDTO response) {
        Product product = new Product();
        Category category = new Category();
        category.setTitle(response.getCategory());

        product.setId(response.getId());
        product.setCategory(category);
        product.setDescription(response.getDescription());
        product.setImageURL(response.getImage());
        product.setTitle(response.getTitle());

        return product;
    }

    @Override
    public Product getProductById(Long id) {
        Product product;

        // call fake store
        ResponseEntity<FakeStoreResponseDTO> fakeStoreResponse =
                restTemplate.getForEntity("https://fakestoreapi.com/products/" + id, FakeStoreResponseDTO.class);

        // get response
        FakeStoreResponseDTO response = fakeStoreResponse.getBody();
        if (response == null) {
            throw new IllegalArgumentException("FakeStoreProduct not found");
        }

        // convert to product model
        product = convertFakeStoreResponseToProduct(response);

        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> response = new ArrayList<>();

        ResponseEntity<FakeStoreResponseDTO[]> fakeStoreProducts =
                restTemplate.getForEntity("https://fakestoreapi.com/products", FakeStoreResponseDTO[].class);

        System.out.println("Status code: " + fakeStoreProducts.getStatusCode());

        for (FakeStoreResponseDTO fakeStoreDTO : fakeStoreProducts.getBody()) {
            response.add(convertFakeStoreResponseToProduct(fakeStoreDTO));
        }

        return response;
    }

    @Override
    public List<Product> getProductsByCategory(String catTitle) {
        List<Product> response = new ArrayList<>();
        FakeStoreResponseDTO[] fakeStoreResponse = restTemplate.getForEntity(
                "https://fakestoreapi.com/products/category/" + catTitle,
                FakeStoreResponseDTO[].class
        ).getBody();

        for (FakeStoreResponseDTO fakeStoreDTO : fakeStoreResponse) {
            response.add(convertFakeStoreResponseToProduct(fakeStoreDTO));
        }

        return response;
    }

    @Override
    public Product createProduct(String title, String description, String imageURL, String catTitle) {
        Product response;
        FakeStoreResponseDTO requestBody = new FakeStoreResponseDTO();
        requestBody.setTitle(title);
        requestBody.setDescription(description);
        requestBody.setImage(imageURL);
        requestBody.setCategory(catTitle);

        ResponseEntity<FakeStoreResponseDTO> fakeStoreResponse =
                restTemplate.postForEntity("https://fakestoreapi.com/products",
                        requestBody,
                        FakeStoreResponseDTO.class);

        System.out.println("Status code: " + fakeStoreResponse.getStatusCode());

        response = convertFakeStoreResponseToProduct(fakeStoreResponse.getBody());
        return response;
    }

    @Override
    public void deleteProductById(Long id) {
        restTemplate.delete("https://fakestoreapi.com/products/" + id);
        System.out.println("Product deleted successfully");
    }

    @Override
    public void updateProduct(Long id, String title, String description, String imageURL, String title1) {
        FakeStoreResponseDTO requestBody = new FakeStoreResponseDTO();
        requestBody.setTitle(title);
        requestBody.setDescription(description);
        requestBody.setImage(imageURL);
        requestBody.setCategory(title1);

        restTemplate.put("https://fakestoreapi.com/products/" + id, requestBody);
        System.out.println("Product updated successfully");
    }

    @Override
    public Page<Product> getPaginatedProducts(int pageNo, int pageSize) {
        return null;
    }
}