package com.aipm.ai_project_management.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Caching configuration for performance optimization.
 * Configures both in-memory caching and Redis caching for different use cases.
 */
@Configuration
@EnableCaching
public class CachingConfig {

    /**
     * Primary cache manager using in-memory caching.
     * Good for development and small-scale deployments.
     */
    @Bean
    @Primary
    public CacheManager inMemoryCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // Define cache names for different types of data
        cacheManager.setCacheNames(java.util.List.of(
            "projects",           // Project data
            "users",              // User information
            "tasks",              // Task data
            "dashboard-metrics",  // Dashboard calculations
            "analytics",          // Analytics data
            "project-health",     // Project health calculations
            "team-performance",   // Team performance metrics
            "notifications",      // User notifications
            "file-metadata"       // File information
        ));
        
        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    /**
     * Redis cache manager for distributed caching.
     * Use when Redis is available for better scalability.
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // Default TTL of 30 minutes
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        // Configure different TTL for different cache types
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Short-lived caches (5 minutes)
        cacheConfigurations.put("dashboard-metrics", 
                defaultCacheConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigurations.put("notifications", 
                defaultCacheConfig.entryTtl(Duration.ofMinutes(5)));
        
        // Medium-lived caches (15 minutes)
        cacheConfigurations.put("tasks", 
                defaultCacheConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigurations.put("team-performance", 
                defaultCacheConfig.entryTtl(Duration.ofMinutes(15)));
        
        // Long-lived caches (1 hour)
        cacheConfigurations.put("projects", 
                defaultCacheConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("users", 
                defaultCacheConfig.entryTtl(Duration.ofHours(1)));
        
        // Very long-lived caches (24 hours)
        cacheConfigurations.put("analytics", 
                defaultCacheConfig.entryTtl(Duration.ofHours(24)));
        cacheConfigurations.put("file-metadata", 
                defaultCacheConfig.entryTtl(Duration.ofHours(24)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}