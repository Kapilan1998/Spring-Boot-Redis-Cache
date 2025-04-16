package com.spring.cache.redis.service;

import com.spring.cache.redis.dto.ProductDto;
import com.spring.cache.redis.entity.Product;
import com.spring.cache.redis.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Forces a cache update after the method is executed.
     * It stores the return value (ProductDto) in the cache.
     * The cache key is result.id() — meaning the product ID returned from the method
    * When we create a new product, we want to add it to the cache immediately so that subsequent getProduct() calls can return it from cache.
     */
    @CachePut(value = "PRODUCT_CACHE", key = "#result.id()")
    public ProductDto createProduct(ProductDto productDto) {
        var product = new Product();
        product.setName(productDto.name());
        product.setPrice(productDto.price());

        Product savedProduct = productRepository.save(product);
        return new ProductDto(savedProduct.getId(), savedProduct.getName(),
                savedProduct.getPrice());
    }

    /**
     * Checks the cache first using productId as the key.
     * If the key is found in cache: returns the cached value, skips executing the method.
     * If the key is not found: runs the method, caches the result using productId as the key, then returns it.
     * When reading data (like a product), this avoids unnecessary DB calls by using cached results whenever possible.
     */
    @Cacheable(value ="PRODUCT_CACHE", key = "#productId")
    public ProductDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id " + productId));
        return new ProductDto(product.getId(), product.getName(),
                product.getPrice());
    }

    /**
     * It uses the returned product ID (#result.id()) as the cache key.
     *After updating a product, you want the cache to reflect the latest data — so @CachePut ensures the cache is refreshed even if the method is always executed.
     */
    @CachePut(value = "PRODUCT_CACHE", key = "#result.id()")
    public ProductDto updateProduct(ProductDto productDto) {
        Long productId = productDto.id();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find product with id " + productId));

        product.setName(productDto.name());
        product.setPrice(productDto.price());

        Product updatedProduct = productRepository.save(product);
        return new ProductDto(updatedProduct.getId(), updatedProduct.getName(),
                updatedProduct.getPrice());
    }

    /**
     *Removes the entry from the cache for the given productId.
     * Makes sure the deleted product is no longer returned from the cache
     * Once a product is deleted from the DB, we don't want stale data in the cache — @CacheEvict ensures that.
     */
    @CacheEvict(value = "PRODUCT_CACHE", key = "#productId")
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
