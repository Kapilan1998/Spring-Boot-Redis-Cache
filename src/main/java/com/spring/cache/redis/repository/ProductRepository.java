package com.spring.cache.redis.repository;

import com.spring.cache.redis.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
