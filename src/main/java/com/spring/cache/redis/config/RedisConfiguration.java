package com.spring.cache.redis.config;

import com.spring.cache.redis.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.time.Duration;


// Marks this class as a source of bean definitions for the Spring context
// this class contains beans that should be managed by the Spring container (this class declares one or more @Bean methods)
@Configuration
public class RedisConfiguration {

    // Defines a RedisCacheManager bean that Spring Boot will use for caching
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        // Creates the default cache configuration
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))         // Sets the time-to-live (TTL) for cached entries to 5 minutes
                .disableCachingNullValues()              // Prevents caching of null values (helps avoid storing unnecessary data)
                .serializeValuesWith(RedisSerializationContext.SerializationPair                     // Sets the serializer for cached values using Jackson to convert ProductDto to JSON
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(ProductDto.class)));

        // Builds and returns a RedisCacheManager using above created configuration
        return RedisCacheManager
                .builder(redisConnectionFactory)             // Uses the Redis connection factory (autowired by Spring)
                .cacheDefaults(redisCacheConfiguration)     // Applies the custom configuration as default
                .build();
    }
}
